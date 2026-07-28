package com.kondo.portfolio.site.notify;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;

import com.kondo.portfolio.entity.ContactMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class DiscordNotifierTest {

    private static final String WEBHOOK_URL = "https://discord.example/api/webhooks/1/abc";
    private static final String ADMIN_URL = "http://example.test:8081/admin/messages";

    private RestClient.Builder builder;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
    }

    private DiscordNotifier notifier(String webhookUrl, String adminUrl) {
        return new DiscordNotifier(builder, webhookUrl, adminUrl);
    }

    private ContactMessage message(String name, String email, String body) {
        ContactMessage m = new ContactMessage();
        m.setName(name);
        m.setEmail(email);
        m.setMessage(body);
        m.setCreatedAt(LocalDateTime.of(2026, 7, 28, 10, 0));
        return m;
    }

    @Test
    void webhookURLが未設定なら送信しない() {
        notifier("", "").notifyReceived(message("太郎", "t@example.com", "本文"));

        // 期待を何も登録していないので、リクエストが飛んでいれば verify で落ちる
        server.verify();
    }

    @Test
    void 受信内容をwebhookにPOSTする() {
        server.expect(requestTo(WEBHOOK_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.embeds[0].title").value("お問い合わせが届きました"))
                .andExpect(jsonPath("$.embeds[0].url").value(ADMIN_URL))
                .andExpect(jsonPath("$.embeds[0].description").value("本文です"))
                .andExpect(jsonPath("$.embeds[0].fields[0].value").value("太郎"))
                .andExpect(jsonPath("$.embeds[0].fields[1].value").value("t@example.com"))
                .andRespond(withNoContent());

        notifier(WEBHOOK_URL, ADMIN_URL).notifyReceived(message("太郎", "t@example.com", "本文です"));

        server.verify();
    }

    @Test
    void 本文に書かれたメンションは飛ばさない() {
        server.expect(requestTo(WEBHOOK_URL))
                .andExpect(jsonPath("$.allowed_mentions.parse").isEmpty())
                .andRespond(withNoContent());

        notifier(WEBHOOK_URL, "").notifyReceived(message("太郎", "t@example.com", "@everyone"));

        server.verify();
    }

    @Test
    void 送信に失敗しても例外を投げない() {
        server.expect(requestTo(WEBHOOK_URL))
                .andRespond(
                        request -> {
                            throw new IOException("接続できない想定");
                        });

        notifier(WEBHOOK_URL, "").notifyReceived(message("太郎", "t@example.com", "本文"));
    }

    @Test
    void 長い本文は途中で切る() {
        String longBody = "あ".repeat(2000);

        DiscordWebhookMessage built =
                notifier(WEBHOOK_URL, ADMIN_URL)
                        .buildMessage(message("太郎", "t@example.com", longBody));

        String description = built.embeds().get(0).description();
        assertThat(description).startsWith("あ".repeat(1500)).endsWith("（長いので省略。続きは管理画面で）");
        assertThat(description.length()).isLessThan(longBody.length());
    }

    @Test
    void 管理画面URLが未設定ならリンクを付けない() {
        DiscordWebhookMessage built =
                notifier(WEBHOOK_URL, "").buildMessage(message("太郎", "t@example.com", "本文"));

        assertThat(built.embeds().get(0).url()).isNull();
    }

    @Test
    void 受信日時をISO8601で載せる() {
        DiscordWebhookMessage built =
                notifier(WEBHOOK_URL, "").buildMessage(message("太郎", "t@example.com", "本文"));

        assertThat(built.embeds().get(0).timestamp()).startsWith("2026-07-28T10:00:00");
    }
}
