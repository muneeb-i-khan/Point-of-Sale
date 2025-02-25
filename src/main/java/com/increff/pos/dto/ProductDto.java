package com.increff.pos.dto;

import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.Normalize;
import com.increff.pos.flow.ProductFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductFlow productFlow;

    public void addProduct(ProductForm productForm) throws ApiException {
        productFlow.addProduct(productForm);
    }

    public ProductData getProduct(Long id) throws ApiException {
        ProductPojo productPojo = productService.getCheck(id);
        return convert(productPojo);
    }

    public List<ProductData> getAllProducts() throws ApiException {
        List<ProductPojo> list = productService.getAllProducts();
        List<ProductData> list2 = new ArrayList<>();
        for(ProductPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void updateProduct(Long id, ProductForm productForm) throws ApiException {
        ProductPojo p = convert(productForm);
        productService.updateProduct(id, p);
    }

    public void uploadProducts(MultipartFile file) throws IOException, ApiException {
        productFlow.uploadProducts(file);
    }

    private ProductData convert(ProductPojo productPojo) {
        ProductData productData = new ProductData();
        productData.setName(Normalize.normalizeName(productPojo.getName()));
        productData.setBarcode(productPojo.getBarcode());
        productData.setId(productPojo.getId());
        productData.setPrice(productPojo.getPrice());
        return productData;
    }

    private ProductPojo convert(ProductForm productForm) {
        ProductPojo p = new ProductPojo();
        p.setName(Normalize.normalizeName(productForm.getName()));
        p.setBarcode(productForm.getBarcode());
        p.setPrice(productForm.getPrice());
        return p;
    }
}
