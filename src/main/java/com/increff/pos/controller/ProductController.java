package com.increff.pos.controller;

import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.dto.ProductDto;
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
        ProductPojo p = ProductDto.convert(ProductForm);
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
        return ProductDto.convert(p);
    }

    @ApiOperation(value = "Get all clients")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll() {
        List<ProductPojo> list = productService.getAllProducts();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for(ProductPojo p : list) {
            list2.add(ProductDto.convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Update an existing product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Long id, @RequestBody ProductForm productForm) throws ApiException{
        ProductPojo p = ProductDto.convert(productForm);
        productService.getCheck(id);
        productService.updateProduct(id,p);
    }
}
