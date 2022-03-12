package com.smart.job;

import com.smart.csv.CsvRow;
import com.smart.mapper.CashboxMapper;
import com.smart.service.Cashbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class CashboxStatsJob implements Runnable {

    private final CashboxMapper cashboxMapper = new CashboxMapper();
    private final List<Cashbox> cashboxes;
    private final Path cashboxStatsPath;

    public CashboxStatsJob(List<Cashbox> cashboxes, Path cashboxStatsPath) {
        this.cashboxes = cashboxes;
        this.cashboxStatsPath = cashboxStatsPath;
    }

    @Override
    public void run() {
        try {
            var csvRows = cashboxes.stream()
                    .map(cashboxMapper::map)
                    .map(CsvRow::toCsvRow)
                    .toList();
            Files.write(cashboxStatsPath, csvRows, CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
