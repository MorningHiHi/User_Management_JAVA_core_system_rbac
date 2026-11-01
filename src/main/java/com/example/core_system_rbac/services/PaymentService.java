package com.example.core_system_rbac.services;

import com.example.core_system_rbac.controllers.NotEnoughMoneyException;
import com.example.core_system_rbac.entities.PaymentDetails;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public PaymentDetails processPayment() {
        throw new NotEnoughMoneyException();//giả sử ném ra một ngoại lệ
    }
}
