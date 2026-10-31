package com.example.core_system_rbac.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Getter
@Setter
@Service
@ApplicationScope
public class LoginCountService {
    private int loginCount;
    public void incrementLoginCount() {
        this.loginCount +=1;
    }
}
