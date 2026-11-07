package com.example.core_system_rbac.security;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(
        basePackages = "com.example.core_system_rbac.client")
public class ProjectConfig {
}
