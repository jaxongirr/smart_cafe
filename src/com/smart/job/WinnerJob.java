package com.smart.job;

import com.smart.csv.BuyerRow;
import com.smart.csv.CashboxRow;
import com.smart.csv.CsvRow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Comparator.comparing;

public class WinnerJob implements Runnable {


    private final Path buyerStatsPath;
    private final Path cashboxStatsPath;

    public WinnerJob(Path buyerStatsPath, Path cashboxStatsPath) {
        this.buyerStatsPath = buyerStatsPath;
        this.cashboxStatsPath = cashboxStatsPath;
    }

    @Override
    public void run() {
        try {
            determineBuyerWinner();
            determineCashboxWinner();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void determineBuyerWinner() throws IOException {
        Files.readAllLines(buyerStatsPath).stream()
                .map(line -> line.split(CsvRow.COLUMN_SEPARATOR))
                .map(BuyerRow::new)
                .max(comparing(buyerRow -> buyerRow.ordersNumber() * buyerRow.orderPriceAvg()))
                .ifPresent(buyerRow -> System.out.println("Buyer winner: " + buyerRow.id()));
    }

    private void determineCashboxWinner() throws IOException {
        Files.readAllLines(cashboxStatsPath).stream()
                .map(line -> line.split(CsvRow.COLUMN_SEPARATOR))
                .map(CashboxRow::new)
                .max(comparing(cashboxRow -> cashboxRow.orderPriceSum() / cashboxRow.ordersNumber()))
                .ifPresent(cashboxRow -> System.out.println("Cashbox winner: " + cashboxRow.id()));
    }
}
