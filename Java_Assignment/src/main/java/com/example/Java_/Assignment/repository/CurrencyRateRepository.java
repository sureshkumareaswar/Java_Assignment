package com.example.Java_.Assignment.repository;

import com.example.Java_.Assignment.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository interface for performing database operations on currency rates.
 */
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    Optional<CurrencyRate> findBySourceCurrencyAndTargetCurrency(String sourceCurrency, String targetCurrency);
    void deleteAllByLastUpdatedBefore(LocalDateTime expirationTime);
}
