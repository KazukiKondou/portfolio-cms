package com.kondo.portfolio.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** 管理画面のエントリポイント (Tailscale からのみアクセスする想定) */
@SpringBootApplication(scanBasePackages = "com.kondo.portfolio")
@EntityScan("com.kondo.portfolio.domain")
@EnableJpaRepositories("com.kondo.portfolio.repository")
public class PortfolioAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioAdminApplication.class, args);
    }
}
