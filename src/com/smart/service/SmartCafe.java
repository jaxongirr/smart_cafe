package com.smart.service;

import com.smart.job.BuyerStatsJob;
import com.smart.job.CashboxStatsJob;
import com.smart.job.WinnerJob;
import com.smart.model.Order;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static com.smart.util.CafeConst.*;

public class SmartCafe extends Thread {

    private final ScheduledExecutorService executorService;
    private final List<Buyer> buyers;
    private final List<Cashbox> cashboxes;
    private final BlockingQueue<Order> allOrders;

    public SmartCafe(int buyersNumber, int cashboxesNumber) {
        this.executorService = Executors.newScheduledThreadPool(3);
        this.allOrders = new ArrayBlockingQueue<>(cashboxesNumber * 10);
        this.buyers = createBuyers(buyersNumber);
        this.cashboxes = createCashboxes(cashboxesNumber);
        initShutdownHook();
    }

    @Override
    public void run() {
        buyers.forEach(buyer -> new Thread(buyer).start());
        cashboxes.forEach(cashbox -> new Thread(cashbox).start());

        var buyerStatsPath = Path.of("resources", "buyer-stats.csv");
        executorService.scheduleAtFixedRate(new BuyerStatsJob(buyers, buyerStatsPath),
                BUYER_STATS_JOB_PERIOD, BUYER_STATS_JOB_PERIOD, TimeUnit.SECONDS);

        var cashboxStatsPath = Path.of("resources", "cashbox-stats.csv");
        executorService.scheduleAtFixedRate(new CashboxStatsJob(cashboxes, cashboxStatsPath),
                CASHBOX_STATS_JOB_PERIOD, CASHBOX_STATS_JOB_PERIOD, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(new WinnerJob(buyerStatsPath, cashboxStatsPath),
                WINNER_JOB_PERIOD, WINNER_JOB_PERIOD, TimeUnit.SECONDS);

    }

    private void initShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown initiated...");
            executorService.shutdown();
            System.out.println("Shutdown finished successfully");
        }));
    }

    private List<Buyer> createBuyers(int buyersNumber) {
        return IntStream.range(0, buyersNumber)
                .mapToObj(i -> new Buyer(allOrders))
                .toList();
    }

    private List<Cashbox> createCashboxes(int cashboxesNumber) {
        return IntStream.range(0, cashboxesNumber)
                .mapToObj(i -> new Cashbox(allOrders))
                .toList();
    }

}
