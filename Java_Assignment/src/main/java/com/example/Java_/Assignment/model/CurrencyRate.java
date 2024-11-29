package com.example.Java_.Assignment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a currency rate stored in the database.
 */
@Data
@Entity
@Table(name = "currency_rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_currency", nullable = false)
    private String sourceCurrency;

    @Column(name = "target_currency", nullable = false)
    private String targetCurrency;

    @Column(name = "conversion_rate", nullable = false)
    private Double conversionRate;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
}
