package com.example.core_system_rbac.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Tên định danh cho cấu hình bảo mật
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Hệ Thống Quản Lý RBAC API")
                        .version("1.0")
                        .description("Tài liệu API cho hệ thống phân quyền"))

                // 1. Khai báo nút Authorize (Định dạng JWT)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))

                // 2. Yêu cầu toàn bộ các API phải đính kèm Token này (ổ khóa sẽ tự động khóa lại)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}