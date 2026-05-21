# portfolio-cms

就活用に作ってるポートフォリオサイト。
CMS で文言を後から書き換えられるようにしたい。

## スタック

- Spring Boot 3.5 (Java 21)
- Thymeleaf
- PostgreSQL (本番) / H2 (開発)
- Flyway

## 起動

```bash
./gradlew bootRun
```

http://localhost:8080 で見られる。

## デプロイ

kondo-home-server 上で docker で動かして、Cloudflare Tunnel で公開する想定。
（まだ未実装、機能7 で着手）
