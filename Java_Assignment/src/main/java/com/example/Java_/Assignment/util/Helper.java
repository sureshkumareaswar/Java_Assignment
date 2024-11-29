package com.example.Java_.Assignment.util;

public class Helper {
    public static String getCurrencyDisplayName(String currencyCode) {
        return switch (currencyCode) {
            case "USD" -> "US Dollar";
            case "EUR" -> "Euro";
            case "INR" -> "Indian Rupee";
            case "GBP" -> "British Pound";
            default -> "Unknown Currency";
        };
    }
}
