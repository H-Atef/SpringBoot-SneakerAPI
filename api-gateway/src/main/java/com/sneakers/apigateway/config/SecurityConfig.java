package com.sneakers.apigateway.config;


import com.sneakers.apigateway.mapper.JwtAuthoritiesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomCorsConfiguration customCorsConfiguration;

    private final String[] freeResourceUrls = {
            "/swagger-ui.html"
            , "/swagger-ui/**"
            , "/v3/api-docs/**"
            , "/swagger-resources/**"
            , "/api-docs/**"
            , "/aggregate/**"
            , "/eureka/**"
    };

    private final String[] sneakersAdminRoleResourceUrls = {
            "/api/v1/sneakers/add-sneakers/**",
            "/api/v1/sneakers/update-sneakers/**",
            "/api/v1/sneakers/delete-sneakers/**",


    };

    private final String[] sneakersFreeResourceUrls = {
            "/api/v1/sneakers",
            "/api/v1/sneakers/search-model-by-name/**",
            "/api/v1/sneakers/find-items-by-category/**",
            "/api/v1/sneakers/get-scraped-sneakers",
            "/api/v1/sneakers/get-and-save-scraped-sneakers",
            "/api/v1/sneakers/get-sneakers-info-by-model-name/**",
            "/api/v1/sneakers/get-sneakers-info-by-category/**"

    };

    private final String[] orderAdminRoleResourceUrls = {
            "/api/v1/order/delete-order/**",
            "/api/v1/order/view-orders"

    };

    private final String[] orderFreeResourceUrls = {
            "/api/v1/order",
            "/api/v1/order/add-order"

    };

    private final String[] inventoryAdminRoleResourceUrls = {
            "/api/v1/inventory/view-inventory-items",
            "/api/v1/inventory/add-item",
            "/api/v1/inventory/delete-item/**",
            "/api/v1/inventory/update-item/**",
    };

    private final String[] inventoryFreeResourceUrls = {
            "/api/v1/inventory"
            , "/api/v1/inventory/check-items"
            , "/api/v1/inventory/check-item/**"
            , "/api/v1/inventory/item-details/**"
            , "/api/v1/inventory/items-details/**"

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(freeResourceUrls).permitAll();
                    requests.requestMatchers(sneakersFreeResourceUrls).permitAll();
                    requests.requestMatchers(sneakersAdminRoleResourceUrls).hasRole("admin");
                    requests.requestMatchers(orderFreeResourceUrls).permitAll();
                    requests.requestMatchers(orderAdminRoleResourceUrls).hasRole("admin");
                    requests.requestMatchers(inventoryFreeResourceUrls).permitAll();
                    requests.requestMatchers(inventoryAdminRoleResourceUrls).hasRole("admin");
                    requests.anyRequest().authenticated();
                })
                .cors(c -> c.configurationSource(customCorsConfiguration))
                .oauth2ResourceServer(auth -> auth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();


    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new JwtAuthoritiesMapper());
        return converter;
    }


}