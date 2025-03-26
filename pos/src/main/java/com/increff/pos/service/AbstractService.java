package com.increff.pos.service;

import com.increff.pos.util.ApiException;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractService {

    protected void isNull(Object object, String message) throws ApiException {
        if (object == null) {
            throw new ApiException(message);
        }
    }

    protected void isNotNull(Object object, String message) throws ApiException {
        if (object != null) {
            throw new ApiException(message);
        }
    }
}