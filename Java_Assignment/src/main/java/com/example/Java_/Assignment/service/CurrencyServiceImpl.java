package com.example.Java_.Assignment.service;

import com.example.Java_.Assignment.DAO.CurrencyRateDAO;
import com.example.Java_.Assignment.configuration.CurrencyBeaconClient;
import com.example.Java_.Assignment.exceptionHandler.CurrencyConversionException;
import com.example.Java_.Assignment.model.CurrencyRate;
import com.example.Java_.Assignment.model.response.CurrencyConversionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
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
        log.info("Starting currency conversion: sourceCurrency={}, targetCurrency={}, amount={}", sourceCurrency, targetCurrency, amount);
        try {
            CurrencyRate rate = currencyRateDAO.findRate(sourceCurrency, targetCurrency);

            if (rate == null || rate.getLastUpdated().isBefore(LocalDateTime.now().minusHours(1))) {
                log.info("No valid rate found or rate expired. Fetching new rate from external API.");
                Double conversionRate = currencyBeaconClient.getConversionRate(sourceCurrency, targetCurrency, amount);
                if (conversionRate != null && conversionRate > 0) {
                    log.info("Fetched conversion rate from API: sourceCurrency={}, targetCurrency={}, rate={}", sourceCurrency, targetCurrency, conversionRate);
                    rate = getCurrencyRate(sourceCurrency, targetCurrency, conversionRate);
                } else {
                    log.error("Failed to fetch conversion rate from API: sourceCurrency={}, targetCurrency={}", sourceCurrency, targetCurrency);
                    throw new CurrencyConversionException("Conversion rate fetch failed from the API.");
                }
            }

            double convertedAmount = amount * rate.getConversionRate();
            BigDecimal roundedAmount = BigDecimal.valueOf(convertedAmount).setScale(2, RoundingMode.HALF_UP);

            log.info("Conversion successful: sourceCurrency={}, targetCurrency={}, originalAmount={}, convertedAmount={}",
                    sourceCurrency, targetCurrency, amount, roundedAmount.doubleValue());

            return new CurrencyConversionResponse(sourceCurrency, targetCurrency, amount, roundedAmount.doubleValue(), "Conversion successful");
        } catch (Exception ex) {
            log.error("Error during currency conversion: sourceCurrency={}, targetCurrency={}, amount={}, error={}",
                    sourceCurrency, targetCurrency, amount, ex.getMessage(), ex);
            throw new CurrencyConversionException("Error converting currency");
        }
    }

    private CurrencyRate getCurrencyRate(String sourceCurrency, String targetCurrency, Double conversionRate) {
        log.info("Saving new currency rate: sourceCurrency={}, targetCurrency={}, conversionRate={}",
                sourceCurrency, targetCurrency, conversionRate);

        CurrencyRate rate = new CurrencyRate();
        rate.setSourceCurrency(sourceCurrency);
        rate.setTargetCurrency(targetCurrency);
        rate.setConversionRate(conversionRate);
        rate.setLastUpdated(LocalDateTime.now());
        currencyRateDAO.saveRate(rate);
        log.info("Currency rate saved successfully: {}", rate);
        return rate;
    }
}
//    public List<CurrencyDropdownItem> getAllCurrencies() {
//        return Stream.of("USD", "EUR", "INR", "GBP")
//                .map(currencyCode -> new CurrencyDropdownItem(currencyCode, getCurrencyDisplayName(currencyCode)))
//                .collect(Collectors.toList());
//    }
