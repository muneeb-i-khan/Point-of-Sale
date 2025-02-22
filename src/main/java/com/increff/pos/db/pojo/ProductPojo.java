package com.increff.pos.db.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
}
