-- 初期データ。テストデータ兼デフォルト値
INSERT INTO site_settings (setting_key, setting_value, description) VALUES
    ('site.title',       'Kondo',                          'サイトタイトル（ヘッダ・h1）'),
    ('hero.tagline',     'Software Engineer / 千葉',       'ヒーローセクションのキャッチ'),
    ('hero.intro',       'Web を中心にバックエンド〜インフラまで触っているエンジニアです。最近は Spring Boot と TypeScript を使うことが多いです。', 'ヒーローセクションの自己紹介'),
    ('footer.copyright', '© 2026 Kondo',                   'フッターのコピーライト'),
    ('nav.home',         'Home',                           'ナビ: ホーム'),
    ('nav.works',        'Works',                          'ナビ: 作品一覧'),
    ('nav.about',        'About',                          'ナビ: 自己紹介'),
    ('nav.contact',      'Contact',                        'ナビ: お問い合わせ');
