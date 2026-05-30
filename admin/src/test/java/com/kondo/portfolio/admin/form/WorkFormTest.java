package com.kondo.portfolio.admin.form;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.Work;
import org.junit.jupiter.api.Test;

class WorkFormTest {

    @Test
    void fromEntity_全フィールドをコピーする() {
        Work w = new Work();
        w.setId(1L);
        w.setTitle("t");
        w.setSummary("s");
        w.setDescription("d");
        w.setUrl("u");
        w.setTags("tag");
        w.setSortOrder(3);
        w.setPublished(true);

        WorkForm f = WorkForm.fromEntity(w);

        assertThat(f.getId()).isEqualTo(1L);
        assertThat(f.getTitle()).isEqualTo("t");
        assertThat(f.getSummary()).isEqualTo("s");
        assertThat(f.getDescription()).isEqualTo("d");
        assertThat(f.getUrl()).isEqualTo("u");
        assertThat(f.getTags()).isEqualTo("tag");
        assertThat(f.getSortOrder()).isEqualTo(3);
        assertThat(f.getPublished()).isTrue();
    }

    @Test
    void applyTo_エンティティに反映する() {
        WorkForm f = new WorkForm();
        f.setTitle("t");
        f.setSortOrder(5);
        f.setPublished(true);

        Work w = new Work();
        f.applyTo(w);

        assertThat(w.getTitle()).isEqualTo("t");
        assertThat(w.getSortOrder()).isEqualTo(5);
        assertThat(w.getPublished()).isTrue();
    }

    @Test
    void applyTo_sortOrderがnullなら0_publishedがnullならfalse() {
        WorkForm f = new WorkForm();
        f.setSortOrder(null);
        f.setPublished(null);

        Work w = new Work();
        f.applyTo(w);

        assertThat(w.getSortOrder()).isZero();
        assertThat(w.getPublished()).isFalse();
    }

    @Test
    void toEntity_新しいエンティティを作る() {
        WorkForm f = new WorkForm();
        f.setTitle("t");
        Work w = f.toEntity();
        assertThat(w.getTitle()).isEqualTo("t");
    }
}
