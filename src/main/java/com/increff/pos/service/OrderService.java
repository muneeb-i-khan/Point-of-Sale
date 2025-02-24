package com.increff.pos.service;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.OrderItemDao;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.db.pojo.OrderItemPojo;
import com.increff.pos.db.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductService productService;


    public OrderPojo createOrder(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        OrderPojo order = new OrderPojo();
        order.setOrderDate("20-Feb-2025");
        double totalAmt = 0.0;
        orderDao.add(order);

        for (OrderItemPojo orderItem : orderItemPojoList) {
            orderItem.setOrder_id(order.getId());
            orderItemDao.add(orderItem);
            ProductPojo productPojo = productService.getProduct(orderItem.getProd_id());
            totalAmt += productPojo.getPrice();
        }
        order.setTotalAmount(totalAmt);
        return order;
    }

    public List<OrderPojo> getAllOrders() {
        return orderDao.selectAll();
    }

    public OrderPojo getOrderById(Long id) throws ApiException {
        return orderDao.selectById(id)
                .orElseThrow(() -> new ApiException("Order with ID " + id + " not found"));
    }

}
