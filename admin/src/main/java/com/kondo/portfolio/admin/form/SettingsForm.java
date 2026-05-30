package com.kondo.portfolio.admin.form;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * /admin/settings のフォームバインディング用
 */
public class SettingsForm {

    private List<Entry> entries = new ArrayList<>();

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    /** key→value の Map に変換する（保存時に SiteSettingService.updateAll に渡す用） */
    public Map<String, String> toUpdates() {
        Map<String, String> updates = new LinkedHashMap<>();
        for (Entry e : entries) {
            updates.put(e.getKey(), e.getValue() == null ? "" : e.getValue());
        }
        return updates;
    }

    public static class Entry {
        private String key;
        private String value;
        private String description;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        /** 長文 or 改行を含むなら textarea で出したい */
        public boolean isLongText() {
            if (value == null) return false;
            return value.length() > 80 || value.contains("\n");
        }
    }
}
