package com.example.core_system_rbac.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class GoldPriceList {
    private List<GoldPriceDetails> symbols;
}
