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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Add a product")
    @PostMapping
    public ProductData addProduct(@Valid @RequestBody ProductForm productForm) throws ApiException {
        return productDto.addProduct(productForm);
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

    @ApiOperation(value = "Update a product")
    @PutMapping("/{id}")
    public ProductData updateProduct(@PathVariable Long id, @RequestBody ProductForm productForm) throws ApiException {
       return productDto.updateProduct(id, productForm);
    }

    @CrossOrigin(origins = "http://localhost:4200", exposedHeaders = "totalProducts")
    @ApiOperation(value = "Get all products with pagination")
    @GetMapping("/paginated")
    public List<ProductData> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        return productDto.getAllProductsPaginated(page, pageSize, httpServletResponse);
    }

    @ApiOperation(value = "check session")
    @PostMapping("/upload")
    public void uploadProducts(@RequestParam("file") MultipartFile file) throws ApiException, IOException {
        productDto.uploadProducts(file);
    }
}
