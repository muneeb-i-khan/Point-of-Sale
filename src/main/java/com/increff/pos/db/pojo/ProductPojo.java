package com.increff.pos.db.pojo;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class ProductPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String barcode;
    @ManyToOne
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "product_client_id_fk"))
    private ClientPojo clientPojo;

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

}
