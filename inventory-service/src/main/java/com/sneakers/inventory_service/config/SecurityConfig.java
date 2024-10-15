package com.sneakers.inventory_service.config;


import com.sneakers.inventory_service.dto.mapper.JwtAuthoritiesMapper;
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
            ,"/api/v1/inventory"
            ,"/api/v1/inventory/check-items"
            ,"/api/v1/inventory/check-item/**"
            ,"/api/v1/inventory/item-details/**"
            ,"/api/v1/inventory/items-details/**"

    };

    private final String[] adminRoleResourceUrls = {
            "/api/v1/inventory/view-inventory-items",
            "/api/v1/inventory/add-item",
            "/api/v1/inventory/delete-item/**",
            "/api/v1/inventory/update-item/**",

    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(freeResourceUrls).permitAll();
                    requests.requestMatchers(adminRoleResourceUrls).hasRole("admin");
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