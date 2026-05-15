package com.sparta.gateway.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.factory.SetStatusGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthenticationEntryPoint
        implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);

        exchange.getResponse()
                .getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        ApiResponse<Void> response = ApiResponse.fail("AUTH_001", "인증이 필요합니다");

        try {

            byte[] body = objectMapper.writeValueAsBytes(response);

            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(body);

            return exchange.getResponse()
                    .writeWith(Mono.just(buffer));

        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
