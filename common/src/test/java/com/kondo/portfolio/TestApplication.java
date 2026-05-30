package com.kondo.portfolio;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** common モジュールのテスト用ブートストラップ（common 自体は Spring Boot アプリではないため） */
@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan("com.kondo.portfolio.entity")
@EnableJpaRepositories("com.kondo.portfolio.repository")
public class TestApplication {}
