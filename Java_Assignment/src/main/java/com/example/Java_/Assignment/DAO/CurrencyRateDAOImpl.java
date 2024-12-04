package com.example.Java_.Assignment.DAO;

import com.example.Java_.Assignment.model.CurrencyRate;
import com.example.Java_.Assignment.repository.CurrencyRateRepository;
import com.example.Java_.Assignment.service.CurrencyRateCleanupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of the CurrencyRateDAO interface.
 */
@Slf4j
@AllArgsConstructor
@Repository
public class CurrencyRateDAOImpl implements CurrencyRateDAO {
    private final CurrencyRateRepository repository;
    private final CurrencyRateCleanupService cleanupService;

    /**
     * Finds a currency rate by source and target currencies.
     *
     * @param sourceCurrency The source currency code.
     * @param targetCurrency The target currency code.
     * @return The found CurrencyRate or null if not found.
     */
    @Override
    public CurrencyRate findRate(String sourceCurrency, String targetCurrency) {
        log.info("Fetching currency rate for sourceCurrency={} and targetCurrency={}", sourceCurrency, targetCurrency);
        cleanupService.deleteExpiredRates();
        Optional<CurrencyRate> rate = repository.findBySourceCurrencyAndTargetCurrency(sourceCurrency, targetCurrency);
        return rate.filter(r -> r.getLastUpdated().isAfter(LocalDateTime.now().minusHours(1))).orElse(null);
    }

    /**
     * Saves a currency rate to the database.
     *
     * @param currencyRate The currency rate to save.
     */
    @Override
    public void saveRate(CurrencyRate currencyRate) {
        log.info("Saving currency rate to database: {}", currencyRate);
        repository.save(currencyRate);
        log.info("Currency rate saved successfully: {}", currencyRate);
    }
}
