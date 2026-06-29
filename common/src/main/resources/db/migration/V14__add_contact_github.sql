-- contact ページに表示する GitHub プロフィール URL
-- 空ならリンクを表示しない。管理画面の設定から後で書き換え可能。
INSERT INTO site_settings (setting_key, setting_value, description) VALUES
    ('contact.github', 'https://github.com/KazukiKondou', 'contact ページに表示する GitHub プロフィールの URL（空なら非表示）');
