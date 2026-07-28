package com.kondo.portfolio.site.notify;

import com.kondo.portfolio.entity.ContactMessage;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

/** お問い合わせを受信したときに Discord へ通知する */
@Component
public class DiscordNotifier {

    private static final Logger log = LoggerFactory.getLogger(DiscordNotifier.class);

    /** 通知に出す送信者名 */
    private static final String USERNAME = "kazukikondo.com";

    /** サイトのアクセント色 (#1B3B6F) */
    private static final int EMBED_COLOR = 0x1B3B6F;

    /** 埋め込みの本文は 4096 文字まで。長文は途中で切る */
    private static final int MESSAGE_MAX_LENGTH = 1500;

    private static final String TRUNCATED_SUFFIX = "…（長いので省略。続きは管理画面で）";

    private final RestClient restClient;
    private final String webhookUrl;
    private final String adminUrl;

    public DiscordNotifier(
            RestClient.Builder builder,
            @Value("${portfolio.notification.discord.webhook-url:}") String webhookUrl,
            @Value("${portfolio.notification.discord.admin-url:}") String adminUrl) {
        this.restClient = builder.build();
        this.webhookUrl = webhookUrl;
        this.adminUrl = adminUrl;
    }

    /** Webhook URL が設定されていれば通知する */
    boolean isEnabled() {
        return StringUtils.hasText(webhookUrl);
    }

    /**
     * 受信したお問い合わせを Discord に流す。
     *
     * <p>フォームの応答を待たせたくないので別スレッドで実行する。通知に失敗してもお問い合わせ自体は 保存済みなので、ログだけ残して先に進む。
     */
    @Async
    public void notifyReceived(ContactMessage message) {
        if (!isEnabled()) {
            log.debug("Discord の Webhook URL が未設定なので通知しない");
            return;
        }
        try {
            restClient
                    .post()
                    .uri(webhookUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(buildMessage(message))
                    .retrieve()
                    .toBodilessEntity();
            log.info("Discord にお問い合わせを通知した: id={}", message.getId());
        } catch (RuntimeException e) {
            log.error("Discord への通知に失敗した: id={}", message.getId(), e);
        }
    }

    /** 通知用の JSON を組み立てる */
    DiscordWebhookMessage buildMessage(ContactMessage message) {
        List<DiscordWebhookMessage.Field> fields =
                List.of(
                        new DiscordWebhookMessage.Field("お名前", message.getName(), true),
                        new DiscordWebhookMessage.Field("メールアドレス", message.getEmail(), true));

        DiscordWebhookMessage.Embed embed =
                new DiscordWebhookMessage.Embed(
                        "お問い合わせが届きました",
                        StringUtils.hasText(adminUrl) ? adminUrl : null,
                        truncate(message.getMessage()),
                        EMBED_COLOR,
                        fields,
                        formatTimestamp(message));

        return new DiscordWebhookMessage(
                USERNAME, List.of(embed), DiscordWebhookMessage.AllowedMentions.none());
    }

    private String truncate(String text) {
        if (text == null || text.length() <= MESSAGE_MAX_LENGTH) {
            return text;
        }
        return text.substring(0, MESSAGE_MAX_LENGTH) + TRUNCATED_SUFFIX;
    }

    /** Discord は ISO8601 の日時を渡すとカードに時刻を表示してくれる */
    private String formatTimestamp(ContactMessage message) {
        if (message.getCreatedAt() == null) {
            return null;
        }
        return message.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toOffsetDateTime()
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
