package com.increff.pos.db.pojo;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
public class InventoryPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long                id;
    @Column(unique = true, nullable = false)
    private String              barcode;
    private Long                quantity;
    double      price;
    @OneToOne
    @JoinColumn(name = "prodId", foreignKey = @ForeignKey(name = "inventory_product_id_fk"))
    private ProductPojo         productPojo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductPojo getProductPojo() {
        return productPojo;
    }

    public void setProductPojo(ProductPojo productPojo) {
        this.productPojo = productPojo;
    }
}
