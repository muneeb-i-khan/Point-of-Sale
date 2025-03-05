package com.increff.pos.dto;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.Normalize;
import com.increff.pos.flow.ProductFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return productFlow.convert(productPojo);
    }

    public List<ProductData> getAllProducts() throws ApiException {
        List<ProductPojo> list = productService.getAllProducts();
        List<ProductData> list2 = new ArrayList<>();
        for(ProductPojo p : list) {
            list2.add(productFlow.convert(p));
        }
        return list2;
    }

    public List<ProductData> getAllProductsPaginated(int page, int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        List<ProductPojo> productPojos = productService.getAllProductsPaginated(page, pageSize);
        Long totalProducts = productService.getProductCount();

        List<ProductData> productDataList = new ArrayList<>();
        for (ProductPojo p : productPojos) {
            productDataList.add(productFlow.convert(p));
        }
        httpServletResponse.setHeader("totalProducts",totalProducts.toString());
        return productDataList;
    }


    public void updateProduct(Long id, ProductForm productForm) throws ApiException {
        ProductPojo p = convert(productForm);
        productService.updateProduct(id, p);
    }

    public void uploadProducts(MultipartFile file) throws IOException, ApiException {
        productFlow.uploadProducts(file);
    }


    private ProductPojo convert(ProductForm productForm) {
        ProductPojo p = new ProductPojo();
        p.setName(Normalize.normalizeName(productForm.getName().trim()));
        p.setBarcode(productForm.getBarcode().trim());
        p.setPrice(productForm.getPrice());
        return p;
    }
}
