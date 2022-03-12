package com.smart;

import com.smart.service.SmartCafe;

public class ApplicationRunner {
    public static void main(String[] args) {
        new SmartCafe(10,5).start();
    }
}
