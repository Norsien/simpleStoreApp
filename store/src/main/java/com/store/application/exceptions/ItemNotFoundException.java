package com.store.application.exceptions;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(String itemName, String variableType, String value) {
        super(itemName + " with " + variableType + ": " + value + " has not been found.");
    }
}
