package com.increff.pos.db.pojo;

import javax.persistence.*;

@Entity
@Table(name = "clients")
public class ClientPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long        id;
    @Column(nullable = false, unique = true)
    private String      name;
    private String      description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
