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

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductDto productDto;
    @PostMapping
    public void addProduct(@Valid @RequestBody ProductForm productForm) throws ApiException {
        productDto.addProduct(productForm);
    }

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

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody ProductForm productForm) throws ApiException {
        productDto.updateProduct(id, productForm);
    }
    
    @ApiOperation(value = "Get all products with pagination")
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize) throws ApiException {
        Map<String, Object> response = productDto.getAllProductsPaginated(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public void uploadProducts(@RequestParam("file") MultipartFile file) throws ApiException, IOException {
        productDto.uploadProducts(file);
    }
}
