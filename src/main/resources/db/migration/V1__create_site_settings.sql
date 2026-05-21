-- サイト全体の文言・設定を key/value で持つテーブル
-- 後で管理画面から編集できるようにする
CREATE TABLE site_settings (
    setting_key   VARCHAR(100) PRIMARY KEY,
    setting_value TEXT         NOT NULL,
    description   VARCHAR(255),
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
