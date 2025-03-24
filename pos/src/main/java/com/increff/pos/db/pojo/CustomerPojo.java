package com.increff.pos.db.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = "phone"))
public class CustomerPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false)
    private String phone;
}
