package com.example.core_system_rbac.client;

import com.example.core_system_rbac.entities.GoldPriceDetails;
import com.example.core_system_rbac.entities.PaymentDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "goldprice",
        url = "${name.service.url}")
public interface GoldPriceProxy {
    @PostMapping("/goldprice")
    GoldPriceDetails createGoldPrice(
            @RequestHeader String requestId,
            @RequestBody GoldPriceDetails goldPrice);
}
