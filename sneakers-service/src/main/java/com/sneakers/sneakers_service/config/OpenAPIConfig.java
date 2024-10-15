package com.sneakers.sneakers_service.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI sneakersServiceAPI(){
        return new OpenAPI()
                .info(new Info().title("Sneakers Service API")
                                .description("REST API for Sneakers Service")
                                .version("v1.0.0")

                        );
    }

}
