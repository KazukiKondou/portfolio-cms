package com.kondo.portfolio.service;

/** 習熟度別にまとめたスキルの表示用 DTO */
public record ProficiencyGroup(int level, String label, String skillsJoined) {

    private static final int MAX_LEVEL = 3;

    /** ●●● / ●●○ / ●○○ の文字列を返す。 習熟度が高い (level=1) ほど ● が多い。 */
    public String dotsString() {
        int filled = Math.max(0, MAX_LEVEL + 1 - level);
        int empty = Math.max(0, MAX_LEVEL - filled);
        return "●".repeat(filled) + "○".repeat(empty);
    }
}
