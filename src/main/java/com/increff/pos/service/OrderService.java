package com.increff.pos.service;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.ProductDao;
import com.increff.pos.db.dao.SalesDao;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.db.pojo.SalesPojo;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.SalesForm;
import com.increff.pos.model.SalesForm.SaleItem;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private SalesDao salesDao;

    @Autowired
    private OrderDao orderDao;

    @Transactional
    public OrderPojo createOrder(SalesForm salesForm) throws ApiException {
        double totalOrderAmount = 0;
        OrderPojo order = new OrderPojo();
        order.setOrderDate(salesForm.getItems().get(0).getSaleDate());

        List<SalesPojo> salesItems = new ArrayList<>();
        for (SaleItem saleItem : salesForm.getItems()) {
            ProductPojo product = productDao.selectByBarcode(saleItem.getBarcode());
            if (product == null) {
                throw new ApiException("Product with barcode " + saleItem.getBarcode() + " not found");
            }

            double unitPrice = product.getPrice();
            double itemTotalAmount = unitPrice * saleItem.getQuantity();
            totalOrderAmount += itemTotalAmount;

            SalesPojo salesPojo = new SalesPojo();
            salesPojo.setProduct(product);
            salesPojo.setQuantity(saleItem.getQuantity());
            salesPojo.setUnitPrice(unitPrice);
            salesPojo.setTotalAmount(itemTotalAmount);
            salesPojo.setSaleDate(saleItem.getSaleDate());
            salesPojo.setOrder(order);
            salesItems.add(salesPojo);
        }

        order.setTotalAmount(totalOrderAmount);
        order.setSalesItems(salesItems);
        orderDao.add(order);

        return order;
    }

    @Transactional
    public List<OrderPojo> getAllOrders() {
        return orderDao.selectAll();
    }

    @Transactional
    public OrderPojo getOrderById(Long id) throws ApiException {
        return orderDao.selectById(id)
                .orElseThrow(() -> new ApiException("Order with ID " + id + " not found"));
    }

    @Transactional
    public void deleteOrder(Long id) throws ApiException {
        OrderPojo order = orderDao.selectById(id)
                .orElseThrow(() -> new ApiException("Order with ID " + id + " not found"));
        orderDao.delete(id);
    }

    @Transactional
    public void updateOrder(Long id, SalesForm salesForm) throws ApiException {
        OrderPojo order = orderDao.selectById(id)
                .orElseThrow(() -> new ApiException("Order with ID " + id + " not found"));

        double totalOrderAmount = 0;
        List<SalesPojo> salesItems = new ArrayList<>();

        for (SaleItem saleItem : salesForm.getItems()) {
            ProductPojo product = productDao.selectByBarcode(saleItem.getBarcode());
            if (product == null) {
                throw new ApiException("Product with barcode " + saleItem.getBarcode() + " not found");
            }

            double unitPrice = product.getPrice();
            double itemTotalAmount = unitPrice * saleItem.getQuantity();
            totalOrderAmount += itemTotalAmount;

            SalesPojo salesPojo = new SalesPojo();
            salesPojo.setProduct(product);
            salesPojo.setQuantity(saleItem.getQuantity());
            salesPojo.setUnitPrice(unitPrice);
            salesPojo.setTotalAmount(itemTotalAmount);
            salesPojo.setSaleDate(saleItem.getSaleDate());
            salesPojo.setOrder(order);
            salesItems.add(salesPojo);
        }

        order.setTotalAmount(totalOrderAmount);
        order.setSalesItems(salesItems);
        orderDao.update(order);
    }
}
