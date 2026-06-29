-- GitHub リンクを contact から about へ移設したのに伴いキー名を中立化する。
-- 値（URL）はそのまま引き継ぐ。空なら about ページで非表示。
UPDATE site_settings
   SET setting_key = 'social.github',
       description = 'about ページに表示する GitHub プロフィールの URL（空なら非表示）'
 WHERE setting_key = 'contact.github';
