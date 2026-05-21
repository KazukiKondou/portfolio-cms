-- サンプル3件。後で管理画面から差し替える想定
INSERT INTO works (title, summary, tags, url, sort_order) VALUES
    ('weather-board',
     '天気予報アプリ。位置情報から3日間の予報を表示する。',
     'React · Express · OpenWeather API',
     'https://github.com/example/weather-board',
     1),
    ('task-tracker',
     '個人用のタスク管理ツール。期限・タグでフィルタできる。',
     'Vue 3 · Spring Boot · PostgreSQL',
     'https://github.com/example/task-tracker',
     2),
    ('lyric-search',
     '歌詞の全文検索エンジン。複数のサイトをクロールして統合。',
     'Next.js · PostgreSQL · Elasticsearch',
     'https://github.com/example/lyric-search',
     3);
