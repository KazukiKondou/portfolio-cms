package com.kondo.portfolio.site.notify;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Discord の Webhook に POST する JSON の形 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DiscordWebhookMessage(
        String username,
        List<Embed> embeds,
        @JsonProperty("allowed_mentions") AllowedMentions allowedMentions) {

    /** 埋め込みカード本体 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Embed(
            String title,
            String url,
            String description,
            Integer color,
            List<Field> fields,
            String timestamp) {}

    /** 埋め込みの中に並べる項目 */
    public record Field(String name, String value, boolean inline) {}

    /** メンションの許可設定 */
    public record AllowedMentions(List<String> parse) {

        /** 本文に @everyone を書かれても実際には飛ばさないように、何も許可しない */
        public static AllowedMentions none() {
            return new AllowedMentions(List.of());
        }
    }
}
