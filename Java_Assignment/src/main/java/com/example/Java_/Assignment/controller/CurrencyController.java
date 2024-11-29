package com.example.Java_.Assignment.controller;

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
        return currencyService.convert(sourceCurrency, targetCurrency, amount);
    }
}
