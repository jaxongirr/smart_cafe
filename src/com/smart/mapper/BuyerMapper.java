package com.smart.mapper;

import com.smart.csv.BuyerRow;
import com.smart.model.Order;
import com.smart.model.Product;
import com.smart.service.Buyer;

import java.util.List;

public class BuyerMapper implements Mapper<Buyer, BuyerRow> {

    @Override
    public BuyerRow map(Buyer buyer) {
        return new BuyerRow(
                buyer.getId(),
                buyer.getOrders().size(),
                getCaloriesAvg(buyer.getOrders()),
                getOrdersPriceAvg(buyer.getOrders())
                );
    }

    private Double getCaloriesAvg(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.products().stream())
                .mapToInt(Product::calories)
                .average()
                .orElse(0.0);
    }

    private Double getOrdersPriceAvg(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.products().stream())
                .mapToInt(Product::price)
                .average()
                .orElse(0.0);
    }
}
