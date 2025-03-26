package com.increff.pos.dto;

import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.service.ClientService;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.Normalize;
import com.increff.pos.flow.ProductFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {
    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductFlow productFlow;

    public ProductData addProduct(ProductForm productForm) throws ApiException {
        ProductPojo productPojo = convert(productForm);
        return convert(productFlow.addProduct(productPojo, productForm.getClientName()));
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

    public List<ProductData> getAllProductsPaginated(int page, int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        List<ProductPojo> productPojos = productService.getAllProductsPaginated(page, pageSize);
        Long totalProducts = productService.getProductCount();

        List<ProductData> productDataList = new ArrayList<>();
        for (ProductPojo p : productPojos) {
            productDataList.add(convert(p));
        }
        httpServletResponse.setHeader("totalProducts",totalProducts.toString());
        return productDataList;
    }


    public ProductData updateProduct(Long id, ProductForm productForm) throws ApiException {
        ProductPojo p = convert(productForm);
        return convert(productService.updateProduct(id, p));
    }

    public void uploadProducts(MultipartFile file, HttpServletResponse response) throws IOException, ApiException {
        productFlow.uploadProducts(file, response);
    }


    private ProductPojo convert(ProductForm productForm) {
        ProductPojo p = new ProductPojo();
        p.setName(Normalize.normalizeName(productForm.getName()));
        p.setBarcode(productForm.getBarcode().trim());
        p.setPrice(productForm.getPrice());
        return p;
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
