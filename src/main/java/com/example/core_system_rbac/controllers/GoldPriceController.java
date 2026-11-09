package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.client.GoldPriceProxy;
import com.example.core_system_rbac.entities.GoldPriceList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GoldPriceController {

    private final GoldPriceProxy goldPriceProxy;

    public GoldPriceController(GoldPriceProxy goldPriceProxy) {
        this.goldPriceProxy = goldPriceProxy;
    }

    @PostMapping("/goldprice")
    public GoldPriceList createPayment(
            //@RequestBody GoldPriceList goldPrice
    ) {
        //String requestId = UUID.randomUUID().toString();
        return goldPriceProxy.createGoldPrice("XAU-USD-SPOT");
    }
}
