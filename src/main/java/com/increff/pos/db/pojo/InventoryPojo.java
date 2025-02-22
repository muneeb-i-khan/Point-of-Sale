package com.increff.pos.db.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "inventory")
public class InventoryPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String barcode;

    private Long quantity;

    @OneToOne
    @JoinColumn(name = "prodId", foreignKey = @ForeignKey(name = "inventory_product_id_fk"))
    private ProductPojo productPojo;
}
