package com.kondo.portfolio.service;

import com.kondo.portfolio.domain.Skill;
import com.kondo.portfolio.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkillService {

    // 習熟度ラベル。後で site_settings に逃がしてもいいが今は固定
    private static final Map<Integer, String> LEVEL_LABELS = Map.of(
            1, "主力",
            2, "よく使う",
            3, "触ったことある"
    );

    private final SkillRepository repository;

    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    /**
     * 公開中のスキルを習熟度ごとにグルーピングして、" · " 連結で返す
     */
    @Transactional(readOnly = true)
    public List<ProficiencyGroup> findGroupedByProficiency() {
        Map<Integer, List<Skill>> grouped = repository
                .findByPublishedTrueOrderByProficiencyAscSortOrderAsc()
                .stream()
                .collect(Collectors.groupingBy(
                        Skill::getProficiency,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return grouped.entrySet().stream()
                .map(e -> new ProficiencyGroup(
                        e.getKey(),
                        LEVEL_LABELS.getOrDefault(e.getKey(), "Level " + e.getKey()),
                        e.getValue().stream()
                                .map(Skill::getName)
                                .collect(Collectors.joining(" · "))
                ))
                .toList();
    }
}
