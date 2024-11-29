package com.example.Java_.Assignment.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

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
     * @param amount The amount to be converted.
     * @return The conversion rate.
     */
    public Double getConversionRate(String sourceCurrency, String targetCurrency, Integer amount) {
        String url = String.format("%s?from=%s&to=%s&amount=%d&api_key=%s",
                apiUrl, sourceCurrency, targetCurrency, amount, apiKey);
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("response")) {
                Map<String, Object> responseData = (Map<String, Object>) response.get("response");
                if (responseData.containsKey("value") && responseData.get("value") != null) {
                    Object value = responseData.get("value");
                    return value instanceof Number ? ((Number) value).doubleValue() : null;
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conversion rate not found in the API response.");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid response structure from Currency Beacon API.");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching conversion rate: " + e.getMessage(), e);
        }
    }
}
