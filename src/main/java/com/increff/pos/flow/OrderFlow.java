package com.increff.pos.flow;

import com.increff.pos.db.dao.OrderItemDao;
import com.increff.pos.db.pojo.OrderItemPojo;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItem;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderFlow {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemDao orderItemDao;

    public void addOrder(List<OrderItemForm> orderItemFormList) throws ApiException {
        List<OrderItemForm> mergedOrderItems = mergeDuplicateItems(orderItemFormList);
        List<OrderItemPojo> orderItemPojoList = convert(mergedOrderItems);
        orderService.createOrder(orderItemPojoList);
    }

    public OrderData getOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderService.getOrderById(id);
        return convert(orderPojo);
    }

    public List<OrderData> getAllOrders() throws ApiException {
        List<OrderPojo> orderPojos = orderService.getAllOrders();
        List<OrderData> orderDataList = new ArrayList<>();

        for (OrderPojo order : orderPojos) {
            orderDataList.add(convert(order));
        }
        return orderDataList;
    }

    private OrderData convert(OrderPojo orderPojo) throws ApiException {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setTotalAmount(orderPojo.getTotalAmount());
        orderData.setOrderDate(orderPojo.getOrderDate());

        List<OrderItemPojo> orderItemPojos = orderItemDao.getItemsByOrderId(orderPojo.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemPojo itemPojo : orderItemPojos) {
            OrderItem orderItem = new OrderItem();
            ProductPojo product = productService.getProduct(itemPojo.getProd_id());
            orderItem.setBarcode(product.getBarcode());
            orderItem.setQuantity(itemPojo.getQuantity().intValue());
            orderItem.setProdName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItems.add(orderItem);
        }

        orderData.setItems(orderItems);
        return orderData;
    }

    private List<OrderItemPojo> convert(List<OrderItemForm> orderItemFormList) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();

        for (OrderItemForm form : orderItemFormList) {
            ProductPojo product = productService.getProductByBarcode(form.getBarcode());

            if (product == null) {
                throw new ApiException("Product with barcode " + form.getBarcode() + " not found");
            }

            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setProd_id(product.getId());
            orderItemPojo.setQuantity(form.getQuantity());

            orderItemPojoList.add(orderItemPojo);
        }

        return orderItemPojoList;
    }

    private List<OrderItemForm> mergeDuplicateItems(List<OrderItemForm> orderItemForms) {
        Map<String, OrderItemForm> mergedItems = new HashMap<>();

        for (OrderItemForm item : orderItemForms) {
            String barcode = item.getBarcode();
            if (mergedItems.containsKey(barcode)) {
                OrderItemForm existingItem = mergedItems.get(barcode);
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            } else {
                mergedItems.put(barcode, item);
            }
        }

        return new ArrayList<>(mergedItems.values());
    }
}
