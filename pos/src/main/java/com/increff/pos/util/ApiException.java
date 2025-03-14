package com.increff.pos.util;

public class ApiException extends RuntimeException{
    public ApiException(String string) {
        super(string);
    }
    private static final long serialVersionUID = 1L;
}