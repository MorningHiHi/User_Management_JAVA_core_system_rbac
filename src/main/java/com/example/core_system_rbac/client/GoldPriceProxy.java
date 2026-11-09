package com.example.core_system_rbac.client;

import com.example.core_system_rbac.entities.GoldPriceList;
import com.example.core_system_rbac.entities.PaymentDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "goldprice",
        url = "${name.service.url}")
public interface GoldPriceProxy {
    @GetMapping("/v1/prices")
    GoldPriceList createGoldPrice(
            @RequestParam String symbol);
            //@RequestBody GoldPriceList goldPrice);
}
