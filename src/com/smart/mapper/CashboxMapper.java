package com.smart.mapper;

import com.smart.csv.CashboxRow;
import com.smart.model.Order;
import com.smart.model.Product;
import com.smart.service.Cashbox;

import java.util.List;

public class CashboxMapper implements Mapper<Cashbox, CashboxRow> {

    @Override
    public CashboxRow map(Cashbox cashbox) {
        return new CashboxRow(
                cashbox.getId(),
                cashbox.getOrders().size(),
                getOrderPriceSum(cashbox.getOrders())
        );
    }

    private Integer getOrderPriceSum(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.products().stream())
                .mapToInt(Product::price)
                .sum();
    }
}
