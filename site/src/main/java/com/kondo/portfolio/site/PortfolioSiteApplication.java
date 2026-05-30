package com.kondo.portfolio.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** 公開サイト (ポートフォリオ本体) のエントリポイント */
@SpringBootApplication(scanBasePackages = "com.kondo.portfolio")
@EntityScan("com.kondo.portfolio.domain")
@EnableJpaRepositories("com.kondo.portfolio.repository")
public class PortfolioSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioSiteApplication.class, args);
    }
}
