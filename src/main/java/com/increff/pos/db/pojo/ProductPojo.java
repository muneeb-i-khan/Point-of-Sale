package com.increff.pos.db.pojo;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class ProductPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String barcode;

    private double price;

    @ManyToOne
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "product_client_id_fk"))
    private ClientPojo clientPojo;

    @OneToOne(mappedBy = "productPojo", cascade = CascadeType.ALL, orphanRemoval = true)
    private InventoryPojo inventory;

    public InventoryPojo getInventory() {
        return inventory;
    }

    public void setInventory(InventoryPojo inventory) {
        this.inventory = inventory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public ClientPojo getClientPojo() {
        return clientPojo;
    }

    public void setClientPojo(ClientPojo clientPojo) {
        this.clientPojo = clientPojo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
