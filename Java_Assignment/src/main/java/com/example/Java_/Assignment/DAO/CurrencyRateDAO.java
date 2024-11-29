package com.example.Java_.Assignment.DAO;

import com.example.Java_.Assignment.model.CurrencyRate;

/**
 * DAO interface for interacting with the currency rates.
 */
public interface CurrencyRateDAO {
    CurrencyRate findRate(String sourceCurrency, String targetCurrency);
    void saveRate(CurrencyRate currencyRate);
}
