package com.example.Java_.Assignment.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyDropdownItem {
    private String currencyCode;
    private String displayName;
}
