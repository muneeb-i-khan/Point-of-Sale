package com.increff.pos.flow;

import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.model.forms.SalesForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderFlowService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    public void createOrderAndUpdateInventory(SalesForm salesForm) throws ApiException {
        validateInventoryAvailability(salesForm.getItems());
        OrderPojo order = orderService.createOrder(salesForm);
        updateInventoryForSales(salesForm.getItems());
    }

    private void validateInventoryAvailability(List<SalesForm.SaleItem> saleItems) throws ApiException {
        for (SalesForm.SaleItem saleItem : saleItems) {
            InventoryPojo inventory = inventoryService.getInventoryByBarcode(saleItem.getBarcode());

            if (inventory.getQuantity() < saleItem.getQuantity()) {
                throw new ApiException("Insufficient inventory for product with barcode: " + saleItem.getBarcode());
            }
        }
    }

    private void updateInventoryForSales(List<SalesForm.SaleItem> saleItems) throws ApiException {
        for (SalesForm.SaleItem saleItem : saleItems) {
            reduceInventory(saleItem.getBarcode(), saleItem.getQuantity());
        }
    }

    private void reduceInventory(String barcode, int quantitySold) throws ApiException {
        InventoryPojo inventory = inventoryService.getInventoryByBarcode(barcode);
        inventory.setQuantity(inventory.getQuantity() - quantitySold);
        inventoryService.updateInventory(inventory.getId(), inventory);
    }
}
