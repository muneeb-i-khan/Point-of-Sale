package com.increff.pos.service;

import com.increff.pos.db.dao.ProductDao;
import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.data.ProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductService {

    private final ProductDao dao;

    @Autowired
    public ProductService(ProductDao dao) {
        this.dao = dao;
    }

    @Autowired
    private ProductFlow productFlow;

    public void addProduct(ProductPojo p) {
        ProductPojo existingProduct = dao.selectByBarcode(p.getBarcode());
        if(existingProduct != null) {
            throw new ApiException("Barcode "+p.getBarcode()+" already exists!");
        }
        dao.add(p);
    }

    public ProductPojo getProductByBarcode(String barcode) throws ApiException {
        ProductPojo product = dao.selectByBarcode(barcode);
        if (product == null) {
            throw new ApiException("Product with the given barcode does not exist: " + barcode);
        }
        return product;
    }

    public ProductPojo getProduct(Long id) throws ApiException {
        return getCheck(id);
    }

    public List<ProductPojo> getAllProducts() {
        return dao.selectAll();
    }

    public ProductData updateProduct(Long id, ProductPojo p) throws ApiException {
        ProductPojo ex = getCheck(id);
        ProductPojo existingProduct = dao.selectByBarcode(p.getBarcode());

        if (existingProduct != null && !existingProduct.getId().equals(id)) {
            throw new ApiException("Barcode already exists for another product.");
        }

        ex.setBarcode(p.getBarcode());
        ex.setName(p.getName());
        ex.setPrice(p.getPrice());
        if (p.getClient_id() != null) {
            ex.setClient_id(p.getClient_id());
        }

        dao.update(ex);
        return productFlow.convert(ex);
    }


    public List<ProductPojo> getAllProductsPaginated(int page, int pageSize) {
        return dao.selectAllPaginated(page, pageSize);
    }

    public Long getProductCount() {
        return dao.countProducts();
    }


    public ProductPojo getCheck(Long id) throws ApiException {
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Product with given ID does not exist, id: " + id);
        }
        return p;
    }
}
