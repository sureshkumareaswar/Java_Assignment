package com.example.Java_.Assignment.service;

import com.example.Java_.Assignment.model.response.CurrencyConversionResponse;
import com.example.Java_.Assignment.model.response.CurrencyDropdownItem;

import java.util.List;

/**
 * Service interface for currency conversion.
 */
public interface CurrencyService {
    CurrencyConversionResponse convert(String sourceCurrency, String targetCurrency, Integer amount);
}
