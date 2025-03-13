package com.increff.pos.flow;

import com.increff.pos.db.dao.OrderItemDao;
import com.increff.pos.db.pojo.*;
import com.increff.pos.dto.CustomerDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderData.OrderItem;
import com.increff.pos.model.forms.CustomerForm;
import com.increff.pos.model.forms.OrderForm.OrderItemForm;
import com.increff.pos.service.InventoryService;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.CustomerService;
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
    private CustomerDto customerDto;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InventoryService inventoryService;

    public OrderData addOrder(List<OrderItemForm> orderItemFormList, CustomerForm customerForm) throws ApiException {
        if (orderItemFormList == null || orderItemFormList.isEmpty()) {
            throw new ApiException("Order cannot be empty.");
        }
        List<OrderItemForm> mergedOrderItems = mergeDuplicateItems(orderItemFormList);
        List<OrderItemPojo> orderItemPojoList = convert(mergedOrderItems);
        CustomerPojo customerPojo = customerDto.convert(customerForm);
        OrderPojo orderPojo = orderService.createOrder(orderItemPojoList, customerPojo);
        return convert(orderPojo);
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

    public void addCustomer(CustomerPojo customerPojo) {
        customerService.addCustomer(customerPojo);
    }

    public InventoryPojo getInventoryByBarcode(String barcode) {
        return inventoryService.getInventoryByBarcode(barcode);
    }

    public ProductPojo getProduct(Long id) {
        return productService.getCheck(id);
    }

    public OrderData convert(OrderPojo orderPojo) throws ApiException {
        if (orderPojo == null) {
            throw new ApiException("Order cannot be null");
        }

        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setTotalAmount(orderPojo.getTotalAmount());
        orderData.setOrderDate(orderPojo.getOrderDate());

        CustomerPojo customer = customerService.getCheck(orderPojo.getId());
        orderData.setCustomerName(customer != null ? customer.getName() : null);
        orderData.setCustomerPhone(customer != null ? customer.getPhone() : null);

        List<OrderItemPojo> orderItemPojos = orderService.getItemsByOrderId(orderPojo.getId());
        if (orderItemPojos == null) {
            throw new ApiException("No items found for order ID: " + orderPojo.getId());
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemPojo itemPojo : orderItemPojos) {
            OrderItem orderItem = new OrderItem();
            ProductPojo product = productService.getCheck(itemPojo.getProdId());
            if (product == null) {
                throw new ApiException("Product not found for ID: " + itemPojo.getProdId() + " in order: " + orderPojo.getId());
            }
            orderItem.setBarcode(product.getBarcode());
            orderItem.setQuantity(itemPojo.getQuantity().intValue());
            orderItem.setProdName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setSellingPrice(itemPojo.getSellingPrice());
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
            orderItemPojo.setProdId(product.getId());
            orderItemPojo.setQuantity(form.getQuantity());
            if(form.getSellingPrice() <= product.getPrice()) {
                orderItemPojo.setSellingPrice(form.getSellingPrice());
            }
            else {
                throw new ApiException("Selling price can't be more than Product's MRP");
            }
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
