package com.increff.pos.service;

import com.increff.pos.db.dao.ProductDao;
import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private final ProductDao dao = new ProductDao();

    @Transactional
    public void addProduct(ProductPojo p) {
        dao.add(p);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getProduct(Long id) throws ApiException {
        return getCheck(id);
    }
    @Transactional
    public List<ProductPojo> getAllProducts() {
        return dao.selectAll();
    }

    @Transactional
    public ProductPojo getCheck(Long id) throws ApiException {
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
        return p;
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateProduct(Long id, ProductPojo p) throws ApiException {
        ProductPojo ex = getCheck(id);
        ex.setBarcode(p.getBarcode());
        ex.setName(p.getName());
        ClientPojo cp = p.getClientPojo();
        ex.setClientPojo(cp);
        dao.update(p);
    }

    @Transactional
    public void deleteProduct(Long id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getProductByBarcode(String barcode) throws ApiException {
        ProductPojo product = dao.selectByBarcode(barcode);
        if (product == null) {
            throw new ApiException("Product with the given barcode does not exist: " + barcode);
        }
        return product;
    }

}
