-- Timeline の各イベントにタグを付けられるようにする
-- 例: 長期インターン → "Java · Spring Boot · PostgreSQL"
ALTER TABLE timeline_events ADD COLUMN tags VARCHAR(300);

-- 既存サンプルにタグを設定
UPDATE timeline_events SET tags = 'Java · Spring Boot · PostgreSQL' WHERE title = '長期インターン参加';
UPDATE timeline_events SET tags = '情報系'                          WHERE title = '大学入学';
