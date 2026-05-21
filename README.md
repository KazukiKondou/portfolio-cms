# portfolio-cms

就活用に作ってるポートフォリオサイト。
管理画面 (Tailscale 経由) で文言や作品を後から書き換えられるようにしてある。

## モジュール構成 (Gradle multi-module)

```
portfolio-cms/
├── common/   共有 (エンティティ / リポジトリ / サービス / Flyway / 共通CSS)
├── site/     公開サイト (Cloudflare Tunnel で外に出す)
└── admin/    管理画面 (Tailscale 経由のみ)
```

| モジュール | エントリポイント | ポート |
|---|---|---|
| `:site` | `com.kondo.portfolio.site.PortfolioSiteApplication` | 8080 |
| `:admin` | `com.kondo.portfolio.admin.PortfolioAdminApplication` | 8081 |

## スタック

- Spring Boot 3.5 (Java 21)
- Thymeleaf
- PostgreSQL (本番) / H2 (開発)
- Flyway
- Spring Security (admin のみ)

## 起動

```bash
# 公開サイト (http://localhost:8080)
./gradlew :site:bootRun

# 管理画面 (http://localhost:8081)
./gradlew :admin:bootRun
```

管理画面のログイン情報はデフォ `admin / admin`。
本番では `ADMIN_USERNAME` / `ADMIN_PASSWORD` 環境変数で必ず上書きする。

## デプロイ (予定)

- `:site` を kondo-home-server で docker 稼働 → Cloudflare Tunnel で公開
- `:admin` を kondo-home-server で docker 稼働 → Tailscale 経由でのみアクセス可能 (bind アドレス制限)
