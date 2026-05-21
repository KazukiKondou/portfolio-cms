-- ヒーローに表示する顔写真の URL を追加
INSERT INTO site_settings (setting_key, setting_value, description) VALUES
    ('hero.photoUrl', '', 'ヒーローの顔写真URL（空ならプレースホルダ表示）');
