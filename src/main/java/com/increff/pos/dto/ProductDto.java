package com.increff.pos.dto;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductDto {
    @Autowired
    static ClientService clientService;

    public static ProductData convert(ProductPojo p) {
        ProductData pd = new ProductData();
        pd.setName(p.getName());
        pd.setBarcode(p.getBarcode());
        pd.setId(p.getId());
        pd.setClientName(p.getClientPojo().getName());
        pd.setClient_id(p.getClientPojo().getId());
        pd.setPrice(p.getPrice());
        return pd;
    }
    public static ProductPojo convert(ProductForm productForm) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setName(productForm.getName());
        p.setBarcode(productForm.getBarcode());
        p.setPrice(productForm.getPrice());
        ClientPojo cp = clientService.getClientByName(productForm.getClientName());
        p.setClientPojo(clientService.getClient(cp.getId()));
        return p;
    }

}
