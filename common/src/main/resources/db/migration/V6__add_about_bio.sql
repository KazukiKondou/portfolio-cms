-- About ページの自己紹介テキスト。段落区切りは空行
INSERT INTO site_settings (setting_key, setting_value, description) VALUES
    ('about.bio',
     '普段は Web 系のエンジニアとして、バックエンドからフロントエンド、インフラまで触ってます。最近メインで使うのは Spring Boot と TypeScript。

技術記事を読んだり、個人プロジェクトをいじったりするのが好きです。',
     'About ページの自己紹介テキスト（空行で段落区切り）');
