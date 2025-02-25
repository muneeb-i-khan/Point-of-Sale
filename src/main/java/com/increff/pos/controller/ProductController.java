package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Post a product")
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductForm productForm) throws ApiException {
        productDto.addProduct(productForm);
        return ResponseEntity.ok("Product added successfully.");
    }

    @ApiOperation(value = "Get a product based on its Id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductData> getProduct(@PathVariable Long id) throws ApiException {
        ProductData product = productDto.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @ApiOperation(value = "Get all products")
    @GetMapping
    public ResponseEntity<List<ProductData>> getAll() throws ApiException {
        List<ProductData> products = productDto.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @ApiOperation(value = "Update an existing product")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductForm productForm) throws ApiException {
        productDto.updateProduct(id, productForm);
        return ResponseEntity.ok("Product updated successfully.");
    }

    @ApiOperation(value = "Upload products via TSV file")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadProducts(@RequestParam("file") MultipartFile file) throws ApiException, IOException {
        productDto.uploadProducts(file);
        return ResponseEntity.ok("Product file uploaded successfully.");
    }
}
