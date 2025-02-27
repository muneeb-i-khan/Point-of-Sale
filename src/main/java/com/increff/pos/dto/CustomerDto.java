package com.increff.pos.dto;

import com.increff.pos.db.pojo.CustomerPojo;
import com.increff.pos.model.forms.CustomerForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerDto {
    @Autowired
    private CustomerService customerService;
    
    public void addCustomer(CustomerForm customerForm) throws ApiException {
        CustomerPojo customerPojo = convert(customerForm);
        customerService.addCustomer(customerPojo);
    }

    public CustomerPojo convert(CustomerForm customerForm) {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setName(customerForm.getName());
        customerPojo.setPhone(customerForm.getPhone());
        return customerPojo;
    }

}
