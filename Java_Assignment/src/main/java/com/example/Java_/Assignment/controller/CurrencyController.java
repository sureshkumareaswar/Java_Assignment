package com.example.Java_.Assignment.controller;

import com.example.Java_.Assignment.exceptionHandler.CurrencyConversionException;
import com.example.Java_.Assignment.model.response.CommonApiResponse;
import com.example.Java_.Assignment.model.response.CurrencyConversionResponse;
import com.example.Java_.Assignment.service.CurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @return A standardized response with the conversion result.
     */
    @PostMapping("/convert")
    public ResponseEntity<CommonApiResponse<CurrencyConversionResponse>> convertCurrency(
            @RequestParam String sourceCurrency,
            @RequestParam String targetCurrency,
            @RequestParam Integer amount) {
        log.info("Received currency conversion request: sourceCurrency={}, targetCurrency={}, amount={}", sourceCurrency, targetCurrency, amount);
        try {
            CurrencyConversionResponse conversionResult = currencyService.convert(sourceCurrency, targetCurrency, amount);
            CommonApiResponse<CurrencyConversionResponse> response = new CommonApiResponse<>
                    (HttpStatus.OK.value(), "Conversion successful", conversionResult);
            log.info("Conversion successful: response={}", response);
            return ResponseEntity.ok(response);
        } catch (CurrencyConversionException ex) {
            log.error("Error during currency conversion: sourceCurrency={}, targetCurrency={}, amount={}, error={}", sourceCurrency, targetCurrency, amount, ex.getMessage(), ex);
            CommonApiResponse<CurrencyConversionResponse> errorResponse = new CommonApiResponse<>
                    (HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
