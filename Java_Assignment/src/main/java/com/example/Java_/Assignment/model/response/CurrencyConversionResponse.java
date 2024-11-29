package com.example.Java_.Assignment.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response structure for currency conversion.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionResponse {
    private String sourceCurrency;
    private String targetCurrency;
    private double amount;
    private double convertedAmount;
    private String message;
}
