package org.example.exception;

public class ValueNotFoundException extends Exception {
    public ValueNotFoundException(String objectName, long id) {
        super(objectName + " with id " + id + " not found");
    }
}
