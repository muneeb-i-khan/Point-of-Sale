package com.increff.pos.flow;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.ClientService;
import com.increff.pos.service.TsvUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ProductFlow {
    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TsvUploadService tsvUploadService;

    public void addProduct(ProductForm productForm) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setName(productForm.getName());
        p.setBarcode(productForm.getBarcode());
        p.setPrice(productForm.getPrice());
        ClientPojo cp = clientService.getClientByName(productForm.getClientName());
        p.setClient_id(cp.getId());
        productService.addProduct(p);
    }

    public void uploadProducts(MultipartFile file) throws IOException, ApiException {
        tsvUploadService.uploadProducts(file);
    }
}
