-- スキルをカテゴリではなく「習熟度」で分類するように変更
-- proficiency: 1 = 主力 / 2 = よく使う / 3 = 触ったことある
ALTER TABLE skills ADD COLUMN proficiency INT NOT NULL DEFAULT 2;

-- 既存データに proficiency を割り当て、sort_order もグループ内で振り直す
UPDATE skills SET proficiency = 1, sort_order = 1 WHERE name = 'Java';
UPDATE skills SET proficiency = 1, sort_order = 2 WHERE name = 'Spring Boot';
UPDATE skills SET proficiency = 1, sort_order = 3 WHERE name = 'TypeScript';
UPDATE skills SET proficiency = 1, sort_order = 4 WHERE name = 'PostgreSQL';

UPDATE skills SET proficiency = 2, sort_order = 1 WHERE name = 'React';
UPDATE skills SET proficiency = 2, sort_order = 2 WHERE name = 'Vue 3';
UPDATE skills SET proficiency = 2, sort_order = 3 WHERE name = 'Docker';
UPDATE skills SET proficiency = 2, sort_order = 4 WHERE name = 'Python';

UPDATE skills SET proficiency = 3, sort_order = 1 WHERE name = 'Go';
UPDATE skills SET proficiency = 3, sort_order = 2 WHERE name = 'Next.js';
UPDATE skills SET proficiency = 3, sort_order = 3 WHERE name = 'AWS';
UPDATE skills SET proficiency = 3, sort_order = 4 WHERE name = 'Cloudflare';

-- カテゴリ列はもう使わないので削除
ALTER TABLE skills DROP COLUMN category;
