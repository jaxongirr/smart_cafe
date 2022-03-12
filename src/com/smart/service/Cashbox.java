package com.smart.service;

import com.smart.model.Order;
import com.smart.util.CafeConst;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Cashbox implements Runnable{

    private static int idGenerator = 1;

    private final Integer id;
    private final List<Order> orders = new ArrayList<>();
    private final BlockingQueue<Order> allOrders;

    public Cashbox(BlockingQueue<Order> allOrders) {
        this.id = idGenerator++;
        this.allOrders = allOrders;
    }

    @Override
    public void run() {
        try {
            var order = allOrders.take();
            Thread.sleep(order.products().size() * CafeConst.MAX_PRODUCT_COUNT * 1000L);
            orders.add(order);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Integer getId() {
        return id;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
