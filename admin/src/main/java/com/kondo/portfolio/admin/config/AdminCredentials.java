package com.kondo.portfolio.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** 管理画面のログイン情報。application.yml の `app.admin.*` を束ねる */
@ConfigurationProperties(prefix = "app.admin")
public record AdminCredentials(String username, String password) {}
