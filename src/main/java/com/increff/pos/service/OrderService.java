package com.increff.pos.service;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.SalesDao;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.db.pojo.SalesPojo;
import com.increff.pos.model.forms.SalesForm;
import com.increff.pos.model.forms.SalesForm.SaleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private SalesDao salesDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductService productService;

    public OrderPojo createOrder(SalesForm salesForm) throws ApiException {
        double totalOrderAmount = 0;
        OrderPojo order = new OrderPojo();
        order.setOrderDate(salesForm.getItems().get(0).getSaleDate());

        List<SalesPojo> salesItems = new ArrayList<>();
        for (SaleItem saleItem : salesForm.getItems()) {
            try {
                ProductPojo product = productService.getProductByBarcode(saleItem.getBarcode());
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
                salesPojo.setProdName(product.getName());
                salesPojo.setOrder(order);
                salesItems.add(salesPojo);
            } catch (javax.persistence.NoResultException e) {
                throw new ApiException("Product with barcode " + saleItem.getBarcode() + " not found");
            }
        }

        order.setTotalAmount(totalOrderAmount);
        order.setSalesItems(salesItems);
        orderDao.add(order);

        return order;
    }


    public List<OrderPojo> getAllOrders() {
        return orderDao.selectAll();
    }

    public OrderPojo getOrderById(Long id) throws ApiException {
        return orderDao.selectById(id)
                .orElseThrow(() -> new ApiException("Order with ID " + id + " not found"));
    }

    public void deleteOrder(Long id) throws ApiException {
        OrderPojo order = orderDao.selectById(id)
                .orElseThrow(() -> new ApiException("Order with ID " + id + " not found"));
        orderDao.delete(id);
    }

    public void updateOrder(Long id, SalesForm salesForm) throws ApiException {
        OrderPojo order = orderDao.selectById(id)
                .orElseThrow(() -> new ApiException("Order with ID " + id + " not found"));

        double totalOrderAmount = 0;
        List<SalesPojo> salesItems = new ArrayList<>();

        for (SaleItem saleItem : salesForm.getItems()) {
            ProductPojo product = productService.getProductByBarcode(saleItem.getBarcode());
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
            salesPojo.setProdName(product.getName());
            salesPojo.setOrder(order);

            salesItems.add(salesPojo);
        }

        order.setTotalAmount(totalOrderAmount);
        order.setSalesItems(salesItems);

        orderDao.update(order);
    }

}
