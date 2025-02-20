package com.increff.pos.dto;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ClientService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.TsvUploadService;
import com.increff.pos.util.Normalize;
import com.increff.pos.util.TsvParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {
    @Autowired
    ClientService clientService;
    @Autowired
    ProductService productService;
    @Autowired
    TsvUploadService tsvUploadService;

    public void addProduct(ProductForm productForm) throws ApiException {
        ProductPojo p = convert(productForm);
        productService.addProduct(p);
    }

    public void deleteProduct(Long id) throws ApiException {
        productService.getCheck(id);
        productService.deleteProduct(id);
    }

    public ProductData getProduct(Long id) throws ApiException {
        productService.getCheck(id);
        ProductPojo p = productService.getProduct(id);
        return convert(p);
    }

    public List<ProductData> getAllProducts() {
        List<ProductPojo> list = productService.getAllProducts();
        List<ProductData> list2 = new ArrayList<>();
        for(ProductPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void updateProduct(Long id, ProductForm productForm) throws ApiException {
        ProductPojo p = convert(productForm);
        productService.getCheck(id);
        productService.updateProduct(id, p);
    }

    public ProductData convert(ProductPojo productPojo) {
        ProductData productData = new ProductData();
        productData.setName(Normalize.normalizeName(productPojo.getName()));
        productData.setBarcode(productPojo.getBarcode());
        productData.setId(productPojo.getId());
        productData.setClientName(productPojo.getClientPojo().getName());
        productData.setClient_id(productPojo.getClientPojo().getId());
        productData.setPrice(productPojo.getPrice());
        return productData;
    }

    public void uploadProducts(MultipartFile file) throws IOException, ApiException {
        tsvUploadService.uploadProducts(file);
    }

    public ProductPojo convert(ProductForm productForm) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setName(Normalize.normalizeName(productForm.getName()));
        p.setBarcode(productForm.getBarcode());
        p.setPrice(productForm.getPrice());
        ClientPojo cp = clientService.getClientByName(productForm.getClientName());
        p.setClientPojo(cp);
        return p;
    }
}
