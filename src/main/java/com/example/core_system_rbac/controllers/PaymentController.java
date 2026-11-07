package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.entities.ErrorDetails;
import com.example.core_system_rbac.entities.PaymentDetails;
import com.example.core_system_rbac.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class PaymentController {
    private final PaymentService paymentService;

    private static Logger logger =
            Logger.getLogger(PaymentController.class.getName());

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public ResponseEntity<?> makePayment(@RequestHeader String requestId , @RequestBody PaymentDetails paymentDetails) {
        //PaymentDetails paymentDetails = paymentService.processPayment();
        logger.info("Received request with ID " + requestId +
                " ;Payment Amount: " + paymentDetails.getAmount());

        paymentDetails.setId(UUID.randomUUID().toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("requestId", requestId)
                .body(paymentDetails);
    }
}
