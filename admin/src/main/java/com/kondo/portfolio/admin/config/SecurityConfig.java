package com.kondo.portfolio.admin.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(AdminCredentials.class)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/admin/login")
                                        .permitAll()
                                        .requestMatchers("/admin/**")
                                        .authenticated()
                                        .anyRequest()
                                        .permitAll())
                .formLogin(
                        form ->
                                form.loginPage("/admin/login")
                                        .loginProcessingUrl("/admin/login")
                                        .defaultSuccessUrl("/admin", true)
                                        .failureUrl("/admin/login?error")
                                        .permitAll())
                .logout(
                        logout ->
                                logout.logoutUrl("/admin/logout")
                                        .logoutSuccessUrl("/admin/login?logout")
                                        .permitAll())
                // 開発時に H2 console を使うので CSRF と frame を緩める
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AdminCredentials creds, PasswordEncoder encoder) {
        UserDetails admin =
                User.builder()
                        .username(creds.username())
                        .password(encoder.encode(creds.password()))
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
