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
- H2 (開発・本番共通、本番では別コンテナで TCP サーバ起動)
- Flyway

## ローカル開発

```bash
# 公開サイト (http://localhost:8080)
./gradlew :site:bootRun

# 管理画面 (http://localhost:8081)
./gradlew :admin:bootRun
```

管理画面のログイン: `admin / admin` （本番は必ず変更）。

## コードフォーマット

Spotless + google-java-format (AOSP スタイル = 4スペース、改行 CRLF) を使用。

```bash
./gradlew spotlessApply   # 整形を適用
./gradlew spotlessCheck   # 整形済みか検証 (build 時にも自動で走る)
```

git blame でフォーマット一括適用のコミットを無視したい場合:

```bash
git config blame.ignoreRevsFile .git-blame-ignore-revs
```

### pre-commit hook (自動整形)

`.githooks/pre-commit` で、コミット前に staged な Java を自動整形する。
clone 後に一度だけ以下を実行して有効化する:

```bash
git config core.hooksPath .githooks
```

これで `git commit` のたびに `spotlessApply` が走り、整形済みの状態でコミットされる。

開発時は ~/portfolio-cms-data/ の H2 ファイルを :site と :admin で共有（AUTO_SERVER モード）。本番では別コンテナの H2 TCP サーバを両方が見る。

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
vi .env  # ADMIN_PASSWORD, ADMIN_BIND, SITE_BIND を編集
docker compose up -d --build
```

3つのコンテナが起動:
- `h2`: 共有 DB (内部ネットワークのみ)
- `site`: 公開サイト (8080)
- `admin`: 管理画面 (8081)

データは Docker volume `h2_data` に永続化される。

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
