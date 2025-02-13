package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.ClientPojo;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ClientDao {
    private HashMap<Integer, ClientPojo> rows;
    private int lastId;
    @PostConstruct
    public void init() {
        rows = new HashMap<>();
    }
    public void add(ClientPojo p) {
        lastId++;
        p.setId(lastId);
        rows.put(p.getId(), p);
    }
    public void delete(int id) {
        rows.remove(id);
    }

    public ClientPojo select(int id) {
        return rows.get(id);
    }

    public List<ClientPojo> selectAll() {
        return new ArrayList<>(rows.values());
    }

    public void update(int id, ClientPojo p) {
        rows.put(id,p);
    }
}