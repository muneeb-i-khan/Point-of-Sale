package com.increff.pos.model;

public class ProductData extends ProductForm{
    private Long        id;
    private Long        client_id;

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
