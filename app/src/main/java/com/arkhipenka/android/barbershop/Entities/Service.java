package com.arkhipenka.android.barbershop.Entities;

import java.util.List;
import java.util.Set;

/**
 * Created by arkhipenka_piotr on 08.12.2017.
 */

public class Service {

    private long id;
    private String name;
    private double price;
    private List<Entry> entries;

    public Service() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}

