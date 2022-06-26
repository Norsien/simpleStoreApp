package com.store.application.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.store.application.content.delivery.Delivery;
import com.store.application.content.delivery.DeliveryDTO;
import com.store.application.content.delivery.DeliveryService;
import com.store.application.content.producer.Producer;
import com.store.application.content.producer.ProducerDTO;
import com.store.application.content.producer.ProducerService;
import com.store.application.content.product.Product;
import com.store.application.content.product.ProductDTO;
import com.store.application.content.product.ProductService;
import com.store.application.content.purchase.Purchase;
import com.store.application.content.purchase.PurchaseDTO;
import com.store.application.content.purchase.PurchaseService;
import com.store.application.content.receipt.Receipt;
import com.store.application.content.receipt.ReceiptDTO;
import com.store.application.content.receipt.ReceiptService;
import com.store.application.content.sale.Sale;
import com.store.application.content.sale.SaleDTO;
import com.store.application.content.sale.SaleService;
import com.store.application.content.stock.Stock;
import com.store.application.content.stock.StockService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@PageTitle("Store")
@Route(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    @Autowired
    DeliveryService deliveryService;
    @Autowired
    ProducerService producerService;
    @Autowired
    ProductService productService;
    @Autowired
    PurchaseService purchaseService;
    @Autowired
    StockService stockService;
    @Autowired
    ReceiptService receiptService;
    @Autowired
    SaleService saleService;

    MemoryBuffer  bufferBuy = new MemoryBuffer();
    Upload buy = new Upload(bufferBuy);
    MemoryBuffer  bufferSell = new MemoryBuffer();
    Upload sell = new Upload(bufferSell);

    public MainView() {
        addClassName("main-view");
        setSizeFull();

        configurate();

        add(
            getContent()
        );
    }

    private Component getContent() {
        Label dost = new Label("Dodaj dostawy");
        Label sprz = new Label("Dodaj sprzedarze");
        VerticalLayout content = new VerticalLayout(dost, buy, sprz, sell);
        content.addClassName("content");
        content.setSizeFull();
        addListener(MainView.SaveDeliveryEvent.class, this::saveDelivery);
        addListener(MainView.SaveProducerEvent.class, this::saveProducer);
        addListener(MainView.SaveProductEvent.class, this::saveProduct);
        addListener(MainView.SavePurchaseEvent.class, this::savePurchase);
        addListener(MainView.SaveReceiptEvent.class, this::saveReceipt);
        addListener(MainView.SaveSaleEvent.class, this::saveSale);
        return content;
    }

    private void configurate(){
        buy.addSucceededListener(event -> {
            InputStream content = bufferBuy.getInputStream();
            String fileName = event.getFileName();
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();

            processTxtDelivery(content);
        });

        sell.addSucceededListener(event -> {
            InputStream content = bufferSell.getInputStream();
            String fileName = event.getFileName();
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();

            processTxtSales(content);
        });
    }

    private void processTxtDelivery(InputStream content) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(content, "UTF-16"));
            String line = br.readLine();
            String[] headers = line.split("\t");
            List<String> list = br.lines().collect(Collectors.toList());

            for (String item1 : list) {
                HashMap<String, String> rowmap = new HashMap<>();

                for(int i=0; i<item1.split("\t").length; i++){
                    rowmap.put(headers[i],item1.split("\t")[i]);
                }
                
                String deliveryCode = rowmap.get("dostawa");
                String date = rowmap.get("data");
                String productCode = rowmap.get("kod produktu");
                String productName = rowmap.get("nazwa produktu");
                String producerCode = rowmap.get("kod producenta");
                String producerName = rowmap.get("nazwa producenta");
                String units = rowmap.get("ile");
                String nettoprice = rowmap.get("cena hurtowa");

                Delivery delivery = deliveryService.get(deliveryCode);
                if (delivery == null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
                    Date parsedDate = dateFormat.parse(date);
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    DeliveryDTO dto = new DeliveryDTO(deliveryCode, timestamp);
                    delivery = dto.toDelivery();
                    fireEvent(new SaveDeliveryEvent(this, delivery));
                }

                Producer producer = producerService.get(producerCode);
                if (producer == null) {
                    ProducerDTO dto = new ProducerDTO(producerName, producerCode, "unknown");
                    producer = dto.toProducer();
                    fireEvent(new SaveProducerEvent(this, producer));
                }
                
                Product product = productService.get(productCode);
                if (product == null) {
                    ProductDTO dto = new ProductDTO(productName, productName.substring(0, 5), "szt", productCode, producerCode);
                    product = dto.toProduct();
                    product.setProducer(producer);
                    Stock stock = new Stock(product);
                    product.setStock(stock);
                    fireEvent(new SaveProductEvent(this, product, stock));
                }

                PurchaseDTO deto = new PurchaseDTO(productCode, deliveryCode, 
                        Double.parseDouble(units.replace(',', '.')), Double.parseDouble(nettoprice.replace(',', '.')));
                Purchase purchase = deto.toPurchase();
                Stock stock = product.getStock();
                stock.setUnits(stock.getUnits().add(purchase.getUnits()));
                purchase.setProduct(product);
                delivery.setNetto(delivery.getNetto().add(purchase.getNettosum()));
                purchase.setDelivery(delivery);
                fireEvent(new SavePurchaseEvent(this, purchase, stock, delivery));
            }
        } catch (IOException e) {

        } catch(Exception e) { 

        }
    }

    private void processTxtSales(InputStream content) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(content, "UTF-16"));
            String line = br.readLine();
            String[] headers = line.split("\t");
            List<String> list = br.lines().collect(Collectors.toList());

            for (String item1 : list) {
                HashMap<String, String> rowmap = new HashMap<>();

                for(int i=0; i<item1.split("\t").length; i++){
                    rowmap.put(headers[i],item1.split("\t")[i]);
                }

                String receiptCode = rowmap.get("rachunek");
                String date = rowmap.get("data");
                String productCode = rowmap.get("kod produktu");
                String productName = rowmap.get("nazwa produktu");
                String units = rowmap.get("ile");
                String netto = rowmap.get("netto");
                String brutto = rowmap.get("brutto");
                String vat = rowmap.get("vat percent");

                Receipt receipt = receiptService.get(receiptCode);
                if (receipt == null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy HH:mm:ss");
                    Date parsedDate = dateFormat.parse(date);
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    ReceiptDTO dto = new ReceiptDTO(receiptCode, timestamp);
                    receipt = dto.toReceipt();
                    fireEvent(new SaveReceiptEvent(this, receipt));
                }

                Product product = productService.get(productCode);

                SaleDTO deto = new SaleDTO(receiptCode, productCode,
                        Double.parseDouble(units.replace(',', '.')),
                        Double.parseDouble(netto.replace(',', '.')),
                        Double.parseDouble(brutto.replace(',', '.')),
                        Double.parseDouble(vat.replace(',', '.')));
                Sale sale = deto.toSale();
                Stock stock = product.getStock();
                stock.setUnits(stock.getUnits().subtract(sale.getUnits()));
                sale.setProduct(product);
                receipt.setNetto(receipt.getNetto().add(sale.getNettosum()));
                receipt.setBrutto(receipt.getBrutto().add(sale.getBruttosum()));
                receipt.setVat(receipt.getVat().add(sale.getVatsum()));
                sale.setReceipt(receipt);
                fireEvent(new SaveSaleEvent(this, sale, stock, receipt));
            }
        } catch (IOException e) {

        } catch(Exception e) { 

        }
    }

    private void saveDelivery(MainView.SaveDeliveryEvent event) {
        deliveryService.save(event.getDelivery());
    }

    public static class SaveDeliveryEvent extends ComponentEvent<MainView> {
        private Delivery delivery;

        protected SaveDeliveryEvent(MainView source, Delivery delivery) {
            super(source, false);
            this.delivery = delivery;
        }

        public Delivery getDelivery() {
            return delivery;
        }
    }

    private void saveProducer(MainView.SaveProducerEvent event) {
        producerService.save(event.getProducer());
    }

    public static class SaveProducerEvent extends ComponentEvent<MainView> {
        private Producer producer;

        protected SaveProducerEvent(MainView source, Producer producer) {
            super(source, false);
            this.producer = producer;
        }

        public Producer getProducer() {
            return producer;
        }
    }

    private void saveProduct(MainView.SaveProductEvent event) {
        productService.save(event.getProduct());
    }
    
    public static class SaveProductEvent extends ComponentEvent<MainView> {
        private Product product;
        private Stock stock;
    
        protected SaveProductEvent(MainView source, Product product, Stock stock) {
            super(source, false);
            this.product = product;
        }
    
        public Product getProduct() {
            return product;
        }
        
        public Stock getStock() {
            return stock;
        }
    }

    private void savePurchase(MainView.SavePurchaseEvent event) {
        purchaseService.save(event.getPurchase());
        stockService.save(event.getStock());
        deliveryService.save(event.getDelivery());
    }
    
    public static class SavePurchaseEvent extends ComponentEvent<MainView>{
        private Stock stock;
        private Delivery delivery;
        private Purchase purchase;
    
        SavePurchaseEvent(MainView source, Purchase purchase, Stock stock, Delivery delivery) {
            super(source, false);
            this.stock = stock;
            this.delivery = delivery;
            this.purchase = purchase;
        }
    
        public Stock getStock() {
            return stock;
        }
    
        public Delivery getDelivery() {
            return delivery;
        }
    
        public Purchase getPurchase() {
            return purchase;
        }
    }

    public static class SaveReceiptEvent extends ComponentEvent<MainView> {
        private Receipt receipt;
    
        protected SaveReceiptEvent(MainView source, Receipt receipt) {
            super(source, false);
            this.receipt = receipt;
        }
    
        public Receipt getReceipt() {
            return receipt;
        }
    }
    
    private void saveReceipt(MainView.SaveReceiptEvent event) {
        receiptService.save(event.getReceipt());
    }

    private void saveSale(MainView.SaveSaleEvent event) {
        saleService.save(event.getSale());
        stockService.save(event.getStock());
        receiptService.save(event.getReceipt());
    }
    
    public static class SaveSaleEvent extends ComponentEvent<MainView>{
        private Stock stock;
        private Receipt receipt;
        private Sale sale;
    
        SaveSaleEvent(MainView source, Sale sale, Stock stock, Receipt receipt) {
            super(source, false);
            this.stock = stock;
            this.receipt = receipt;
            this.sale = sale;
        }
    
        public Stock getStock() {
            return stock;
        }
    
        public Receipt getReceipt() {
            return receipt;
        }
    
        public Sale getSale() {
            return sale;
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
        ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
}

}
    
