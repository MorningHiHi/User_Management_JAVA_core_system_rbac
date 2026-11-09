package com.example.core_system_rbac.entities;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class GoldPriceDetails {

    private String symbol;
    private String quote_currency;
    private String unit;
    private String contract_type;
    private double price;
    private Boolean is_stale;
    private OffsetDateTime computed_at;
}
