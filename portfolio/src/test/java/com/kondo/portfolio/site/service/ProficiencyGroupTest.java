package com.kondo.portfolio.site.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProficiencyGroupTest {

    @Test
    void レベル1は黒丸3つ() {
        assertThat(new ProficiencyGroup(1, "主力", "Java").dotsString()).isEqualTo("●●●");
    }

    @Test
    void レベル2は黒丸2白丸1() {
        assertThat(new ProficiencyGroup(2, "よく使う", "React").dotsString()).isEqualTo("●●○");
    }

    @Test
    void レベル3は黒丸1白丸2() {
        assertThat(new ProficiencyGroup(3, "触ったことある", "Go").dotsString()).isEqualTo("●○○");
    }
}
