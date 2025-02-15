package com.increff.pos.service;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.ProductDao;
import com.increff.pos.db.dao.SalesDao;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.db.pojo.SalesPojo;
import com.increff.pos.model.SalesForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

        String currentOrderDate = salesForm.getItems().get(0).getSaleDate();
        order.setOrderDate(currentOrderDate);

        List<SalesPojo> salesItems = new ArrayList<>();
        for (SalesForm.SaleItem saleItem : salesForm.getItems()) {
            ProductPojo product = productDao.selectByBarcode(saleItem.getBarcode());
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
}
