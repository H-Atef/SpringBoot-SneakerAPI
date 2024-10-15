# SneakrAPI

## Overview

SneakrAPI is a Spring Boot-based microservices project that provides a comprehensive platform for managing sneakers inventory, orders, and user authentication. The project consists of three main services: Inventory Service, Sneakers Service, and Order Service. These services are designed to work together seamlessly, leveraging various dependencies and tools to ensure efficient communication, security, and scalability.

## Project Aims

SneakrAPI aims to provide a comprehensive platform for managing sneakers inventory, orders, and user authentication. The project focuses on delivering real-time sneakers information, enabling users to search and manage sneakers efficiently. Additionally, the project is designed to be scalable, allowing for future enhancements to ordering methods and the inclusion of additional services, such as notification system.

## Services

### Inventory Service

- Responsible for managing sneakers inventory, including CRUD operations and stock management.

### Sneakers Service

- Handles sneakers-related operations, such as retrieving sneaker information, searching by category and model name, and scraping data from external sources using the Sneakers Scraper API (Django project).
- Communicates with the Inventory Service to retrieve and update sneaker stock information.
- Integrates with the Sneakers Scraper API (Django project) to provide users with information on sneakers availability and details.

### Order Service

- Manages order-related operations, including creating, updating, and canceling orders.
- Interacts with the Inventory Service to update stock levels and ensure order fulfillment.

## Dependencies and Tools

- **Eureka Discovery Server:** Provides a database of available service instances, enabling services to be discovered, registered, and de-registered.
- **API Gateway:** Simplifies API design, improves performance, and enhances flexibility by routing APIs through a single entry point.
- **Keycloak:** Serves as the authorization server, handling OAuth2 and JWT-based authentication and authorization.
- **Sync Communication Tools:** Utilizes WebClient, RestClient, Feign Client, and RestTemplate for synchronous communication between services.
- **Resilience4j:** Configures circuit breakers to detect and handle service unavailability and request timeouts.
- **OpenAPI:** Provides documentation for available endpoints for each service.

## Future Enhancements

- Enhance ordering methods to include more payment options and shipping integrations.
- Integrate notification service to inform users of order updates.
- Expand sneakers information to include additional details, such as reviews and ratings.
