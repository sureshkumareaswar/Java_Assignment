package com.example.Java_.Assignment.service;

import com.example.Java_.Assignment.repository.CurrencyRateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@Service
public class CurrencyRateCleanupService {
    private final CurrencyRateRepository repository;

    /**
     * Deletes expired rates from the database.
     * A rate is considered expired if its last updated time is older than 1 hour.
     */
    @Transactional
    public void deleteExpiredRates() {
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(1);
        log.info("Deleting expired currency rates older than {}", expirationTime);
        repository.deleteAllByLastUpdatedBefore(expirationTime);
        log.info("Expired currency rates deleted successfully.");
    }
}
