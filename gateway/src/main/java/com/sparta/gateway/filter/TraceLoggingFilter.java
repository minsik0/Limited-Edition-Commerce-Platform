package com.sparta.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class TraceLoggingFilter implements GlobalFilter {

    private static final String TRACE_ID = "X-Trace-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("TRACE FILTER LOADED");
        String traceId = exchange.getRequest()
                .getHeaders()
                .getFirst(TRACE_ID);

        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }

        final String finalTraceId = traceId;
        long start = System.currentTimeMillis();

        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header(TRACE_ID, traceId)
                .build();

        ServerWebExchange mutatedExchange = exchange
                .mutate()
                .request(mutatedRequest)
                .build();

        String method = String.valueOf(mutatedRequest.getMethod());
        String path = mutatedRequest.getURI().getPath();

        log.info("[TRACE_ID={}] {} {} START", traceId, method, path);

        return chain.filter(mutatedExchange)
                .doFinally(signalType -> {

                    long duration = System.currentTimeMillis() - start;

                    log.info("[TRACE_ID={}] {} {} END {}ms", finalTraceId, method, path, duration);
                });
    }
}
