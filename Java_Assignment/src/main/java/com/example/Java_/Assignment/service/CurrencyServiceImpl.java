package com.example.Java_.Assignment.service;

import com.example.Java_.Assignment.DAO.CurrencyRateDAO;
import com.example.Java_.Assignment.configuration.CurrencyBeaconClient;
import com.example.Java_.Assignment.exceptionHandler.CurrencyConversionException;
import com.example.Java_.Assignment.model.CurrencyRate;
import com.example.Java_.Assignment.model.response.CurrencyConversionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRateDAO currencyRateDAO;
    private final CurrencyBeaconClient currencyBeaconClient;

    /**
     * Converts the given amount from source currency to target currency.
     *
     * @param sourceCurrency The source currency code.
     * @param targetCurrency The target currency code.
     * @param amount         The amount to convert.
     * @return A CurrencyConversionResponse containing the conversion details.
     */
    @Override
    public CurrencyConversionResponse convert(String sourceCurrency, String targetCurrency, Integer amount) {
        try {
            CurrencyRate rate = currencyRateDAO.findRate(sourceCurrency, targetCurrency);

            if (rate == null || rate.getLastUpdated().isBefore(LocalDateTime.now().minusHours(1))) {
                // No valid rate found in the database or rate is expired (older than 1 hour)
                Double conversionRate = currencyBeaconClient.getConversionRate(sourceCurrency, targetCurrency, amount);
                if (conversionRate != null && conversionRate > 0) {
                    rate = getCurrencyRate(sourceCurrency, targetCurrency, conversionRate);
                } else {
                    throw new CurrencyConversionException("Conversion rate fetch failed from the API.");
                }
            }
            double convertedAmount = amount * rate.getConversionRate();
            return new CurrencyConversionResponse(sourceCurrency, targetCurrency, amount, convertedAmount, "Conversion successful");
        } catch (Exception ex) {
            throw new CurrencyConversionException("Error converting currency");
        }
    }

    private CurrencyRate getCurrencyRate(String sourceCurrency, String targetCurrency, Double conversionRate) {
        CurrencyRate rate;
        rate = new CurrencyRate();
        rate.setSourceCurrency(sourceCurrency);
        rate.setTargetCurrency(targetCurrency);
        rate.setConversionRate(conversionRate);
        rate.setLastUpdated(LocalDateTime.now());
        currencyRateDAO.saveRate(rate);
        return rate;
    }
}
//    public List<CurrencyDropdownItem> getAllCurrencies() {
//        return Stream.of("USD", "EUR", "INR", "GBP")
//                .map(currencyCode -> new CurrencyDropdownItem(currencyCode, getCurrencyDisplayName(currencyCode)))
//                .collect(Collectors.toList());
//    }
