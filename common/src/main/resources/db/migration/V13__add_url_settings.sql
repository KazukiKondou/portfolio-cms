-- 公開サイト / 管理画面の URL を設定として持つ
-- admin の「サイトを見る」リンクで urls.site が使われる
-- 本番では管理画面から自分のドメインに書き換える
INSERT INTO site_settings (setting_key, setting_value, description) VALUES
    ('urls.site',  'http://localhost:8080', '公開サイトの URL（admin から開くときに使う）'),
    ('urls.admin', 'http://localhost:8081', '管理画面の URL（情報表示用、変更してもリンクは差し替わらない）');
