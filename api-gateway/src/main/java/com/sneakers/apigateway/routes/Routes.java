package com.sneakers.apigateway.routes;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class Routes {
    @Bean
    RouterFunction<ServerResponse> sneakersServiceRoute() {
        return GatewayRouterFunctions.route("sneakers_service")
                .route(RequestPredicates.path("/api/v1/sneakers/**"),
                        HandlerFunctions.http("http://localhost:8083"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("sneakersServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")
                        ))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> sneakersServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("sneakers_service_swagger")
                .route(RequestPredicates.path("/aggregate/sneakers-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8083"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("sneakersServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")
                ))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> inventoryServiceRoute() {
        return GatewayRouterFunctions.route("inventory_service")
                .route(RequestPredicates.path("/api/v1/inventory/**"),
                        HandlerFunctions.http("http://localhost:8084"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")
                ))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8084"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")
                ))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }


    @Bean
    RouterFunction<ServerResponse> orderServiceRoute() {
        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/v1/order/**"),
                        HandlerFunctions.http("http://localhost:8082"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")
                ))
                .build();
    }


    @Bean
    RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8082"))
                .filter(FilterFunctions.setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")
                ))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> discoveryServerRoute() {
        return GatewayRouterFunctions.route("discovery_server")
                .route(RequestPredicates.path("/eureka/web"),
                        HandlerFunctions.http("http://localhost:8761"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("discoveryServerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")
                ))
                .filter(FilterFunctions.setPath("/"))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> discoveryServerStaticRoute() {
        return GatewayRouterFunctions.route("discovery_server_static")
                .route(RequestPredicates.path("/eureka/**"),
                        HandlerFunctions.http("http://localhost:8761"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("discoveryServerStaticCircuitBreaker",
                        URI.create("forward:/fallbackRoute")
                ))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> fallbackRoute() {
        return GatewayRouterFunctions.route("fallbackRoute")
                .GET("fallbackRoute",
                        request -> ServerResponse
                                .status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Unfortunately, service is unavailable at the moment, please try again later."))
                .build();
    }




}
