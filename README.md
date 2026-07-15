# Airline API Gateway

A Spring Cloud Gateway microservices API gateway for an airline system that routes incoming client requests to multiple downstream microservices with JWT-based authentication, role-based access control, and secure request handling.

## Overview

The Airline API Gateway serves as the central entry point for all client requests in the airline system. It handles:
- **Intelligent request routing** to Auth, Flight, and Booking microservices
- **JWT-based authentication** with token validation
- **Role-Based Access Control (RBAC)** with multiple user roles (ADMIN, AIRLINE_MANAGER, CUSTOMER, SUPER_ADMIN)
- **Reactive/async request processing** using Spring WebFlux
- **Centralized security configuration** and error handling
- **Comprehensive authorization** for different API endpoints

## Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.5.4 + Spring Cloud Gateway (WebFlux)
- **Runtime:** Reactive (WebFlux) with non-blocking async processing
- **Authentication:** JWT (JJWT 0.12.6) with HMAC-SHA signing
- **Security:** Spring Security 6.x with reactive authentication
- **Build Tool:** Maven 3.x (with Maven Wrapper)
- **Key Libraries:**
  - `spring-cloud-starter-gateway-server-webflux` - API Gateway routing
  - `spring-boot-starter-security` - Security framework
  - `jjwt` - JWT creation and validation
  - `lombok` - Boilerplate reduction

## How It's Organized
```

airline_api_gateway/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
├── .gitattributes
├── .mvn/
│   └── wrapper/
│       ├── maven-wrapper.jar
│       ├── maven-wrapper.properties
│       └── MavenWrapperDownloader.java
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/airline/gateway/
    │   │       ├── GatewayApplication.java
    │   │       ├── config/
    │   │       │   ├── SecurityConfig.java
    │   │       │   └── StartupListener.java
    │   │       ├── security/
    │   │       │   ├── JwtService.java
    │   │       │   ├── JwtAuthenticationManager.java
    │   │       │   ├── JwtAuthenticationConverter.java
    │   │       │   ├── JwtAuthenticationEntryPoint.java
    │   │       │   ├── JwtAccessDeniedHandler.java
    │   │       │   └── JwtAuthenticationFilter.java
    │   │       └── utils/
    │   │           └── ApiResponse.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/
            └── com/airline/gateway/
                └── GatewayApplicationTests.java


```


## Directory Descriptions

### 📁 `config/`
Contains Spring Bean configurations and security setup.

| File | Purpose |
|------|---------|
| **SecurityConfig.java** | Defines the reactive security web filter chain, authorization rules per endpoint, role-based access control (RBAC), JWT authentication filter integration, and CSRF disable for API gateway |
| **StartupListener.java** | Listens to application startup events and logs the gateway's readiness status with URL, port, and health information |

### 📁 `security/`
Complete JWT authentication and authorization implementation stack.

| File | Purpose |
|------|---------|
| **JwtService.java** | Validates JWT signatures, extracts claims (username, role, userId, expiration), parses tokens, and handles JWT exceptions |
| **JwtAuthenticationManager.java** | Implements reactive Spring `ReactiveAuthenticationManager`, validates tokens via JwtService, creates Authentication objects with roles as granted authorities |
| **JwtAuthenticationConverter.java** | Extracts Bearer token from HTTP `Authorization` header and converts it to an Authentication object for processing |
| **JwtAuthenticationEntryPoint.java** | Catches unauthenticated requests (no/invalid token) and returns standardized 401 Unauthorized JSON response |
| **JwtAccessDeniedHandler.java** | Catches authenticated but unauthorized requests (insufficient role) and returns standardized 403 Forbidden JSON response |
| **JwtAuthenticationFilter.java** | Stub for additional authentication filtering (currently minimal implementation) |

### 📁 `utils/`
Shared utility classes and response models.

| File | Purpose |
|------|---------|
| **ApiResponse.java** | Generic response wrapper class used across the gateway for consistent JSON responses with success flag, status code, message, data, error list, timestamp, path, and trace ID |

## How It Fits Together

### Request Flow Architecture
