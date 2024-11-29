package com.example.Java_.Assignment;

import com.example.Java_.Assignment.DAO.CurrencyRateDAO;
import com.example.Java_.Assignment.configuration.CurrencyBeaconClient;
import com.example.Java_.Assignment.model.CurrencyRate;
import com.example.Java_.Assignment.model.response.CurrencyConversionResponse;
import com.example.Java_.Assignment.service.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyServiceImplTest {

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Mock
    private CurrencyRateDAO currencyRateDAO;

    @Mock
    private CurrencyBeaconClient currencyBeaconClient;

    private CurrencyRate currencyRate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyRate = new CurrencyRate();
        currencyRate.setSourceCurrency("USD");
        currencyRate.setTargetCurrency("EUR");
        currencyRate.setConversionRate(0.85);
        currencyRate.setLastUpdated(LocalDateTime.now());
    }

    @Test
    public void testConvertCurrency_WithValidRate() {
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        Integer amount = 100;
        when(currencyRateDAO.findRate(sourceCurrency, targetCurrency)).thenReturn(currencyRate);

        CurrencyConversionResponse response = currencyService.convert(sourceCurrency, targetCurrency, amount);
        assertNotNull(response);
        assertEquals("USD", response.getSourceCurrency());
        assertEquals("EUR", response.getTargetCurrency());
        assertEquals(100, response.getAmount());
        assertEquals(85.0, response.getConvertedAmount(), 0.01);
        assertEquals("Conversion successful", response.getMessage());
        verify(currencyRateDAO, times(1)).findRate(sourceCurrency, targetCurrency);
    }

    @Test
    public void testConvertCurrency_WithNoRateAndFetchFromAPI() {
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        Integer amount = 100;

        when(currencyRateDAO.findRate(sourceCurrency, targetCurrency)).thenReturn(null);
        when(currencyBeaconClient.getConversionRate(sourceCurrency, targetCurrency, amount)).thenReturn(0.85);

        CurrencyConversionResponse response = currencyService.convert(sourceCurrency, targetCurrency, amount);
        assertNotNull(response);
        assertEquals("USD", response.getSourceCurrency());
        assertEquals("EUR", response.getTargetCurrency());
        assertEquals(100, response.getAmount());
        assertEquals(85.0, response.getConvertedAmount(), 0.01);
        assertEquals("Conversion successful", response.getMessage());
        verify(currencyRateDAO, times(1)).findRate(sourceCurrency, targetCurrency);
        verify(currencyBeaconClient, times(1)).getConversionRate(sourceCurrency, targetCurrency, amount);
    }
}
