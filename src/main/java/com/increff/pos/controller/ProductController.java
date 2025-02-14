package com.increff.pos.controller;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ClientService;
import com.increff.pos.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class ProductController {
    @Autowired
    ClientService clientService;
    @Autowired
    ProductService productService;
    @ApiOperation(value = "Post a product")
    @RequestMapping(path="/api/product/", method = RequestMethod.POST)
    public void addProduct(@RequestBody ProductForm ProductForm) throws ApiException{
        ProductPojo p = convert(ProductForm);
        productService.addProduct(p);
    }

    @ApiOperation(value = "Delete a product")
    @RequestMapping(path="/api/product/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws ApiException {
        productService.getCheck(id);
        productService.deleteProduct(id);
    }

    @ApiOperation(value = "Get a product based on it's Id")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Long id) throws ApiException {
        productService.getCheck(id);
        ProductPojo p = productService.getProduct(id);
        return convert(p);
    }

    @ApiOperation(value = "Get all clients")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll() {
        List<ProductPojo> list = productService.getAllProducts();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for(ProductPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Update an existing product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Long id, @RequestBody ProductForm productForm) throws ApiException{
        ProductPojo p = convert(productForm);
        productService.getCheck(id);
        productService.updateProduct(id,p);
    }

    public static ProductData convert(ProductPojo p) {
        ProductData pd = new ProductData();
        pd.setName(p.getName());
        pd.setBarcode(p.getBarcode());
        pd.setId(p.getId());
        pd.setClientName(p.getClientPojo().getName());
        pd.setClient_id(p.getClientPojo().getId());
        return pd;
    }
    public ProductPojo convert(ProductForm productForm) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setName(productForm.getName());
        p.setBarcode(productForm.getBarcode());
        ClientPojo cp = clientService.getClientByName(productForm.getClientName());
        p.setClientPojo(clientService.getClient(cp.getId()));
        return p;
    }

}
