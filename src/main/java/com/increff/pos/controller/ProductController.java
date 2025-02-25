package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductDto productDto;

    @ApiOperation(value = "Post a product")
    @PostMapping
    public void addProduct(@RequestBody ProductForm productForm) throws ApiException {
        productDto.addProduct(productForm);
    }

//    @ApiOperation(value = "Delete a product")
//    @DeleteMapping("/{id}")
//    public void deleteProduct(@PathVariable Long id) throws ApiException {
//        productDto.deleteProduct(id);
//    }

    @ApiOperation(value = "Get a product based on its Id")
    @GetMapping("/{id}")
    public ProductData getProduct(@PathVariable Long id) throws ApiException {
        return productDto.getProduct(id);
    }

    @ApiOperation(value = "Get all products")
    @GetMapping
    public List<ProductData> getAll() throws ApiException {
        return productDto.getAllProducts();
    }

    @ApiOperation(value = "Update an existing product")
    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody ProductForm productForm) throws ApiException {
        productDto.updateProduct(id, productForm);
    }


    @ApiOperation(value = "Upload products via TSV file")
    @PostMapping("/upload")
    public void uploadProducts(@RequestParam("file") MultipartFile file) throws ApiException, IOException {
        productDto.uploadProducts(file);
    }
}