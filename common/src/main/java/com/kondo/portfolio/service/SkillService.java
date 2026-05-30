package com.kondo.portfolio.service;

import com.kondo.portfolio.domain.Skill;
import com.kondo.portfolio.repository.SkillRepository;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkillService {

    // 習熟度ラベル。後で site_settings に逃がしてもいいが今は固定
    private static final Map<Integer, String> LEVEL_LABELS =
            Map.of(
                    1, "主力",
                    2, "よく使う",
                    3, "触ったことある");

    private final SkillRepository repository;

    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    /** 公開中のスキルを習熟度ごとにグルーピングして、" · " 連結で返す */
    @Transactional(readOnly = true)
    public List<ProficiencyGroup> findGroupedByProficiency() {
        Map<Integer, List<Skill>> grouped =
                repository.findByPublishedTrueOrderByProficiencyAscSortOrderAsc().stream()
                        .collect(
                                Collectors.groupingBy(
                                        Skill::getProficiency,
                                        LinkedHashMap::new,
                                        Collectors.toList()));

        return grouped.entrySet().stream()
                .map(
                        e ->
                                new ProficiencyGroup(
                                        e.getKey(),
                                        LEVEL_LABELS.getOrDefault(
                                                e.getKey(), "Level " + e.getKey()),
                                        e.getValue().stream()
                                                .map(Skill::getName)
                                                .collect(Collectors.joining(" · "))))
                .toList();
    }

    /** 全件返す（管理画面用、未公開も含む） */
    @Transactional(readOnly = true)
    public List<Skill> findAllOrdered() {
        return repository.findAll(
                Sort.by("proficiency").ascending().and(Sort.by("sortOrder").ascending()));
    }

    @Transactional(readOnly = true)
    public Optional<Skill> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Skill save(Skill skill) {
        LocalDateTime now = LocalDateTime.now();
        if (skill.getId() == null) {
            skill.setCreatedAt(now);
        }
        skill.setUpdatedAt(now);
        if (skill.getPublished() == null) skill.setPublished(true);
        if (skill.getSortOrder() == null) skill.setSortOrder(0);
        if (skill.getProficiency() == null) skill.setProficiency(2);
        return repository.save(skill);
    }

    /** id で既存レコードを引いて applier で変更を当て、保存する。 */
    @Transactional
    public Optional<Skill> update(Long id, Consumer<Skill> applier) {
        return repository
                .findById(id)
                .map(
                        existing -> {
                            applier.accept(existing);
                            return save(existing);
                        });
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
