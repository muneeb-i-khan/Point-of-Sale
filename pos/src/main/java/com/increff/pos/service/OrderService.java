package com.increff.pos.service;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.OrderItemDao;
import com.increff.pos.db.pojo.*;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderDao orderDao;

    public void createOrderItem(OrderItemPojo orderItemPojo) {
        orderItemDao.add(orderItemPojo);
    }

    public void createOrder(OrderPojo orderPojo) {
            orderDao.add(orderPojo);
    }

    public List<OrderPojo> getAllOrders() {
        return orderDao.selectAll();
    }

    public OrderPojo getOrderById(Long id) throws ApiException {
        return orderDao.selectById(id)
                .orElseThrow(() -> new ApiException("Order with ID " + id + " not found"));
    }

    public int countOrdersByDate(ZonedDateTime date) {
        return orderDao.countOrdersByDate(date);
    }

    public int countItemsSoldByDate(ZonedDateTime date) {
        return orderDao.countItemsSoldByDate(date);
    }

    public Double calculateRevenueByDate(ZonedDateTime date) {
        return orderDao.calculateRevenueByDate(date);
    }

    public List<OrderPojo> getAllOrdersPaginated(int page, int pageSize) {
        return orderDao.selectAllPaginated(page, pageSize);
    }

    public Long getOrderCount() {
        return orderDao.countOrders();
    }

    public List<OrderItemPojo> getItemsByOrderId(Long id) {
        return orderItemDao.getItemsByOrderId(id);
    }

}