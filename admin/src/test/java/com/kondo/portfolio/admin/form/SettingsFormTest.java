package com.kondo.portfolio.admin.form;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SettingsFormTest {

    private SettingsForm.Entry entry(String key, String value) {
        SettingsForm.Entry e = new SettingsForm.Entry();
        e.setKey(key);
        e.setValue(value);
        return e;
    }

    @Test
    void toUpdates_keyからvalueのMapにする() {
        SettingsForm form = new SettingsForm();
        form.getEntries().add(entry("site.title", "Kondo"));
        form.getEntries().add(entry("nav.home", "Home"));

        assertThat(form.toUpdates())
                .containsEntry("site.title", "Kondo")
                .containsEntry("nav.home", "Home");
    }

    @Test
    void toUpdates_valueがnullなら空文字にする() {
        SettingsForm form = new SettingsForm();
        form.getEntries().add(entry("key", null));
        assertThat(form.toUpdates()).containsEntry("key", "");
    }

    @Test
    void entry_長文判定_80文字超はtrue() {
        SettingsForm.Entry e = entry("k", "a".repeat(81));
        assertThat(e.isLongText()).isTrue();
    }

    @Test
    void entry_長文判定_改行含むはtrue() {
        SettingsForm.Entry e = entry("k", "a\nb");
        assertThat(e.isLongText()).isTrue();
    }

    @Test
    void entry_長文判定_短文はfalse() {
        SettingsForm.Entry e = entry("k", "短い");
        assertThat(e.isLongText()).isFalse();
    }

    @Test
    void entry_長文判定_nullはfalse() {
        SettingsForm.Entry e = entry("k", null);
        assertThat(e.isLongText()).isFalse();
    }
}
