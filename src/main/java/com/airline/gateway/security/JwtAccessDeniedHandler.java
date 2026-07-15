package com.airline.gateway.security;

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

@Component
public class JwtAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange,
                             AccessDeniedException ex) {

        ServerHttpResponse response =
                exchange.getResponse();

        response.setStatusCode(HttpStatus.FORBIDDEN);

        response.getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        ApiResponse<?> apiResponse =
                ApiResponse.failed(
                        "Access Denied"
                );

        try {

            byte[] bytes =
                    objectMapper.writeValueAsBytes(apiResponse);

            DataBuffer buffer =
                    response.bufferFactory().wrap(bytes);

            return response.writeWith(Mono.just(buffer));

        } catch (Exception e) {

            byte[] bytes =
                    "{\"success\":false,\"message\":\"Access Denied\"}"
                            .getBytes(StandardCharsets.UTF_8);

            DataBuffer buffer =
                    response.bufferFactory().wrap(bytes);

            return response.writeWith(Mono.just(buffer));
        }
    }
}
