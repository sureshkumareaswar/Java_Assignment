package com.example.Java_.Assignment.controller;

import com.example.Java_.Assignment.exceptionHandler.CurrencyConversionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import com.example.Java_.Assignment.model.response.CurrencyConversionResponse;
import com.example.Java_.Assignment.model.response.CurrencyDropdownItem;
import com.example.Java_.Assignment.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;
import java.util.List;

/**
 * Controller for handling currency conversion requests.
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    /**
     * Converts currency based on the provided parameters.
     *
     * @param sourceCurrency The source currency code.
     * @param targetCurrency The target currency code.
     * @param amount         The amount to convert.
     * @return A CurrencyConversionResponse containing the conversion result.
     */
    @PostMapping("/convert")
    public CurrencyConversionResponse convertCurrency(@RequestParam String sourceCurrency,
                                                      @RequestParam String targetCurrency,
                                                      @RequestParam Integer amount) {
        log.info("Received currency conversion request: sourceCurrency={}, targetCurrency={}, amount={}",
                sourceCurrency, targetCurrency, amount);
        try {
            CurrencyConversionResponse response = currencyService.convert(sourceCurrency, targetCurrency, amount);
            log.info("Conversion successful: sourceCurrency={}, targetCurrency={}, amount={}, response={}",
                    sourceCurrency, targetCurrency, amount, response);
            return response;
        } catch (CurrencyConversionException ex) {
            log.error("Error during currency conversion: sourceCurrency={}, targetCurrency={}, amount={}, error={}",
                    sourceCurrency, targetCurrency, amount, ex.getMessage(), ex);
            throw ex;
        }
    }
}
