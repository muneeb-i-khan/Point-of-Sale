package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ProductController {
    @Autowired
    ProductDto productDto;

    @ApiOperation(value = "Post a product")
    @RequestMapping(path="/api/product/", method = RequestMethod.POST)
    public void addProduct(@RequestBody ProductForm productForm) throws ApiException{
        productDto.addProduct(productForm);
    }

    @ApiOperation(value = "Delete a product")
    @RequestMapping(path="/api/product/{id}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable Long id) throws ApiException {
        productDto.deleteProduct(id);
    }

    @ApiOperation(value = "Get a product based on it's Id")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData getProduct(@PathVariable Long id) throws ApiException {
        return productDto.getProduct(id);
    }

    @ApiOperation(value = "Get all clients")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll() {
            return productDto.getAllProducts();
    }

    @ApiOperation(value = "Update an existing product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public void updateProduct(@PathVariable Long id, @RequestBody ProductForm productForm) throws ApiException{
        productDto.updateProduct(id, productForm);
    }
}
