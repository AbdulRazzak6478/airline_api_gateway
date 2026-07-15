package com.airline.gateway.security;

import com.airline.gateway.utils.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class JwtAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @Override
    public Mono<Void> handle(ServerWebExchange exchange,
                             AccessDeniedException ex) {

        System.out.println("inside AccessDeniedHandler");
        ServerHttpResponse response =
                exchange.getResponse();

        response.setStatusCode(HttpStatus.FORBIDDEN);

        response.getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        ApiResponse<?> apiResponse =
                ApiResponse.builder()
                        .path(exchange.getRequest().getURI().getPath())
                        .errors(List.of("Access Denied"))
                        .message("You Are Not Authorized to this resource")
                        .status(HttpStatus.FORBIDDEN.value())
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .traceId("Request Id")
                        .build();

        System.out.println(apiResponse);
        try {

            byte[] bytes =
                    objectMapper.writeValueAsBytes(apiResponse);

            DataBuffer buffer =
                    response.bufferFactory().wrap(bytes);

            System.out.println("inside DataBuffer ");
            return response.writeWith(Mono.just(buffer));

        } catch (Exception e) {

            System.out.println("inside catch buffer : "+e.getMessage());
            byte[] bytes =
                    "{\"success\":false,\"message\":\"Access Denied\"}"
                            .getBytes(StandardCharsets.UTF_8);

            DataBuffer buffer =
                    response.bufferFactory().wrap(bytes);

            return response.writeWith(Mono.just(buffer));
        }
    }
}
