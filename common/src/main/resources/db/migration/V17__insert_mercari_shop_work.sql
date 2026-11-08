-- メルカリでの3Dプリント品販売を作品に1件足す。並びは今ある作品の最後
-- 同じタイトルを管理画面から先に登録していた場合は足さない
INSERT INTO works (title, summary, description, url, tags, sort_order)
SELECT
    'こんどぅ工房｜3Dプリント',
    '3人のチームで、建築模型用の家具や車を3Dプリントで作ってメルカリで売っている。',
    '自分の担当は企画・調査・タスクの割り振り・お金の管理。売り切れ商品の相場から競合のいない枠を探して、形は寸法から Python で生成している。',
    'https://jp.mercari.com/user/profile/542836628',
    'メルカリ · 3Dプリント · Python · チーム運営',
    COALESCE(MAX(sort_order), 0) + 1
FROM works
WHERE NOT EXISTS (SELECT 1 FROM works WHERE title = 'こんどぅ工房｜3Dプリント');
