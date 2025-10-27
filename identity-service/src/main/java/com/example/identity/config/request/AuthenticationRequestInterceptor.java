package com.example.identity.config.request;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes == null) {
            log.warn("No request attributes found — skipping Authorization header propagation");
            return;
        }

        var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");

        log.info("Header: {}", authHeader);
        if (StringUtils.hasText(authHeader)) template.header("Authorization", authHeader);
    }
}
