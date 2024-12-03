package com.example.Java_.Assignment.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@Component
public class CurrencyBeaconClient {

    @Value("${currencybeacon.api.url}")
    private String apiUrl;

    @Value("${currencybeacon.api.key}")
    private String apiKey;

    /**
     * Fetches the conversion rate from the Currency Beacon API.
     *
     * @param sourceCurrency The source currency code (e.g., USD).
     * @param targetCurrency The target currency code (e.g., INR).
     * @param amount         The amount to be converted.
     * @return The conversion rate.
     */
    public Double getConversionRate(String sourceCurrency, String targetCurrency, Integer amount) {
        String url = String.format("%s?from=%s&to=%s&amount=%d&api_key=%s",
                apiUrl, sourceCurrency, targetCurrency, amount, apiKey);
        log.info("Fetching conversion rate from API. URL: {}", url);
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("response")) {
                Map<String, Object> responseData = (Map<String, Object>) response.get("response");
                if (responseData.containsKey("value") && responseData.get("value") != null) {
                    Object value = responseData.get("value");
                    log.info("Conversion rate fetched successfully: {} -> {} for amount {} is {}", sourceCurrency, targetCurrency, amount, value);
                    return value instanceof Number ? ((Number) value).doubleValue() : null;
                } else {
                    log.warn("Conversion rate not found in the API response for sourceCurrency={} and targetCurrency={}", sourceCurrency, targetCurrency);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conversion rate not found in the API response.");
                }
            } else {
                log.error("Invalid response structure from API for sourceCurrency={} and targetCurrency={}", sourceCurrency, targetCurrency);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid response structure from Currency Beacon API.");
            }
        } catch (Exception e) {
            log.error("Error fetching conversion rate for sourceCurrency={} and targetCurrency={}: {}", sourceCurrency, targetCurrency, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching conversion rate: " + e.getMessage(), e);
        }
    }
}
