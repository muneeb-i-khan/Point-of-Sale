package com.increff.pos.flow;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.ClientService;
import com.increff.pos.util.Normalize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ProductFlow {
    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TsvUploadFlow tsvUploadFlow;

    public ProductData addProduct(ProductForm productForm) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setName(productForm.getName());
        p.setBarcode(productForm.getBarcode());
        p.setPrice(productForm.getPrice());

        ClientPojo clientPojo;
        clientPojo = clientService.getCheck(productForm.getClientName());
        p.setClientId(clientPojo.getId());
        productService.addProduct(p);
        return convert(p);
    }

    public void uploadProducts(MultipartFile file, HttpServletResponse response) throws IOException, ApiException {
        tsvUploadFlow.uploadProducts(file, response);
    }

    public ProductData convert(ProductPojo productPojo) throws ApiException{
        ProductData productData = new ProductData();
        productData.setName(Normalize.normalizeName(productPojo.getName()));
        productData.setBarcode(productPojo.getBarcode());
        productData.setId(productPojo.getId());
        productData.setClientName(clientService.getCheck(productPojo.getClientId()).getName());
        productData.setPrice(productPojo.getPrice());
        return productData;
    }
}
