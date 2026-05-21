package com.kondo.portfolio.service;

import com.kondo.portfolio.domain.Skill;
import com.kondo.portfolio.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private final SkillRepository repository;

    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    /**
     * 公開中のスキルをカテゴリでグルーピングして、" · " 連結した文字列で返す。
     * 例: { "Languages": "Java · TypeScript · ...", "Frameworks": "..." }
     */
    @Transactional(readOnly = true)
    public Map<String, String> findGroupedByCategoryAsString() {
        return repository.findByPublishedTrueOrderBySortOrderAsc()
                .stream()
                .collect(Collectors.groupingBy(
                        Skill::getCategory,
                        LinkedHashMap::new,
                        Collectors.mapping(Skill::getName, Collectors.joining(" · "))
                ));
    }
}
