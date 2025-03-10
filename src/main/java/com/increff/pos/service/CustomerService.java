package com.increff.pos.service;

import com.increff.pos.db.dao.CustomerDao;
import com.increff.pos.db.pojo.CustomerPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class CustomerService {
    @Autowired
    private CustomerDao dao;

    public void addCustomer(CustomerPojo p) throws ApiException{
        CustomerPojo prevCustomerPojo = getPhone(p.getPhone());
        if(prevCustomerPojo != null) {
            updateCustomer(prevCustomerPojo.getId(),p);
            return;
        }
        dao.add(p);
    }

    public CustomerPojo getCustomer(Long id) throws ApiException {
        return getCheck(id);
    }

    private CustomerPojo getPhone(String number) throws ApiException {
        return dao.selectPhone(number);
    }

    public List<CustomerPojo> getAllCustomers() {
        return dao.selectAll();
    }

    public void updateCustomer(Long id, CustomerPojo p) throws ApiException {
        CustomerPojo ex = getCheck(id);
        ex.setName(p.getName());
        ex.setPhone(p.getPhone());
        dao.update(ex);
    }


    public CustomerPojo getCheck(Long id) throws ApiException {
        CustomerPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Customer with given ID does not exist: " + id);
        }
        return p;
    }
}
