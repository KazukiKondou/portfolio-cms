package com.kondo.portfolio.admin.service;

import com.kondo.portfolio.entity.Skill;
import com.kondo.portfolio.repository.SkillRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 管理画面用のスキルサービス（CRUD） */
@Service
public class SkillService {

    private final SkillRepository repository;

    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    /** 全件返す（未公開も含む） */
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
