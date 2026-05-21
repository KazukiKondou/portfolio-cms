# portfolio-cms

就活用に作ってるポートフォリオサイト。
管理画面 (Tailscale 経由) で文言・作品・スキル・顔写真を後から書き換えられるようにしてある。

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
- Spring Security (admin のみ)
- PostgreSQL (本番) / H2 (開発)
- Flyway

## ローカル開発

```bash
# 公開サイト (http://localhost:8080)
./gradlew :site:bootRun

# 管理画面 (http://localhost:8081)
./gradlew :admin:bootRun
```

管理画面のログイン: `admin / admin` （本番は必ず変更）。

⚠️ 開発時は :site と :admin で別の H2 in-memory を使っているので、片方で書き換えてももう片方には反映されない。本番では同じ PostgreSQL を見るので問題なし。

## CMS で編集できるもの

| URL | 内容 |
|---|---|
| `/admin/settings` | サイトの文言（タイトル / ヒーローキャッチ / 自己紹介文 / フッターコピーライト / ナビ）|
| `/admin/media` | ヒーローの顔写真 |
| `/admin/works` | 作品一覧（追加・編集・削除）|
| `/admin/skills` | スキル一覧（追加・編集・削除、習熟度別）|
| `/admin/messages` | お問い合わせフォームの受信一覧 |

## デプロイ (kondo-home-server)

```bash
# 初回
cp .env.example .env
vi .env  # POSTGRES_PASSWORD, ADMIN_PASSWORD, ADMIN_BIND を編集
docker compose up -d --build
```

### Cloudflare Tunnel (公開サイト用)

`site` コンテナはホストの `127.0.0.1:8080` にバインドされている。
Cloudflare Tunnel の ingress 設定で `localhost:8080` を `kondo.kazukikondo.com`（など）にマップする。

```yaml
# ~/.cloudflared/config.yml
ingress:
  - hostname: kondo.kazukikondo.com
    service: http://localhost:8080
  - service: http_status:404
```

### Tailscale (管理画面用)

`admin` コンテナはデフォルトで `127.0.0.1:8081` バインド。
Tailscale 経由で他デバイスからアクセスしたい場合、`.env` に Tailscale IP を設定:

```bash
# kondo-home-server の tailscale IP を確認
tailscale ip -4

# .env で設定
ADMIN_BIND=100.x.y.z
```

これで Cloudflare Tunnel には載せず、Tailscale ネットワーク内のデバイスだけが `http://100.x.y.z:8081/admin` でアクセスできる。
