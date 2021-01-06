package org.example.handle;

import java.math.BigDecimal;

public class Order {

    private String id;
    private String username;
    private BigDecimal price;

    public Order(String id, String username, BigDecimal price) {
        this.id = id;
        this.username = username;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
