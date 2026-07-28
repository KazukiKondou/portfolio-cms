package com.kondo.portfolio.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/** 公開サイト (ポートフォリオ本体) のエントリポイント */
@SpringBootApplication(scanBasePackages = "com.kondo.portfolio")
@EntityScan("com.kondo.portfolio.entity")
@EnableJpaRepositories("com.kondo.portfolio.repository")
// お問い合わせの Discord 通知をフォーム送信と切り離して動かすため
@EnableAsync
public class PortfolioSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioSiteApplication.class, args);
    }
}
