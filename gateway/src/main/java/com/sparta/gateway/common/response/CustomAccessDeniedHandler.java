package com.sparta.gateway.common.response;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {

        exchange.getResponse()
                .setStatusCode(HttpStatus.FORBIDDEN);

        exchange.getResponse()
                .getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        ApiResponse<Void> response = ApiResponse.fail("AUTH_002", "접근 권한이 없습니다");

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
