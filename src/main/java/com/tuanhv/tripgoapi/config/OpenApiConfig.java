package com.tuanhv.tripgoapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tripGoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TripGo API")
                        .version("v1")
                        .description(
                                "REST API cho ứng dụng đặt tour TripGo"
                        )
                )
                .tags(List.of(
                        new Tag()
                                .name("01. Authentication")
                                .description("Đăng ký, đăng nhập và thông tin user"),

                        new Tag()
                                .name("02. Destinations")
                                .description("Danh sách điểm đến"),

                        new Tag()
                                .name("03. Categories")
                                .description("Danh mục tour"),

                        new Tag()
                                .name("04. Tours")
                                .description("Tìm kiếm và thông tin tour"),

                        new Tag()
                                .name("05. Bookings")
                                .description("Đặt tour và quản lý booking")
                ))
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
