package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.client.GoldPriceProxy;
import com.example.core_system_rbac.entities.GoldPriceDetails;
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
    public GoldPriceDetails createPayment(
            //@RequestBody GoldPriceDetails goldPrice
    ) {
        String requestId = UUID.randomUUID().toString();
        return goldPriceProxy.createGoldPrice(requestId);
    }
}
