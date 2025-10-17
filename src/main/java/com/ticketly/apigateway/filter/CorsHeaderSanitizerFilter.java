package com.ticketly.apigateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Ensures that downstream CORS headers are de-duplicated before the response is committed.
 */
@Component
public class CorsHeaderSanitizerFilter implements GlobalFilter, Ordered {

    private static final List<String> CORS_HEADERS = List.of(
        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
        HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
        HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
        HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
        HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
        HttpHeaders.ACCESS_CONTROL_MAX_AGE
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getResponse().beforeCommit(() -> {
            sanitize(exchange.getResponse().getHeaders());
            return Mono.empty();
        });
        return chain.filter(exchange);
    }

    private void sanitize(HttpHeaders headers) {
        for (String header : CORS_HEADERS) {
            List<String> values = headers.get(header);
            if (values != null && values.size() > 1) {
                headers.set(header, values.get(values.size() - 1));
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
