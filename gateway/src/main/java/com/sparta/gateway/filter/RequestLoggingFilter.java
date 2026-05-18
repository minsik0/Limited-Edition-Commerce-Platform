package com.sparta.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class RequestLoggingFilter implements GlobalFilter {

    private static final String TRACE_ID = "X-Trace-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String traceId = UUID.randomUUID().toString();

        String method = String.valueOf(exchange.getRequest().getMethod());

        long start = System.currentTimeMillis();

        String path = exchange.getRequest()
                        .getURI()
                        .getPath();

        log.info("[TRACE_ID={}] {} {}", traceId, method, path);

        return chain.filter(exchange.mutate().request(exchange
                                .getRequest()
                                .mutate()
                                .header(TRACE_ID, traceId)
                                .build()
                        )
                        .build()
        ).doFinally(signalType -> {

            long duration = System.currentTimeMillis() - start;

            log.info("[TRACE_ID={}] {} {} END {}ms", traceId, method, path, duration
            );
        });
    }
}
