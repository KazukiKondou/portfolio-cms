package com.kondo.portfolio.admin.form;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.TimelineEvent;
import org.junit.jupiter.api.Test;

class TimelineEventFormTest {

    @Test
    void fromEntity_コピーする() {
        TimelineEvent e = new TimelineEvent();
        e.setId(1L);
        e.setYear(2020);
        e.setMonth(4);
        e.setTitle("入学");
        e.setDescription("d");
        e.setTags("Java");
        e.setSortOrder(1);
        e.setPublished(true);

        TimelineEventForm f = TimelineEventForm.fromEntity(e);

        assertThat(f.getId()).isEqualTo(1L);
        assertThat(f.getYear()).isEqualTo(2020);
        assertThat(f.getMonth()).isEqualTo(4);
        assertThat(f.getTitle()).isEqualTo("入学");
        assertThat(f.getDescription()).isEqualTo("d");
        assertThat(f.getTags()).isEqualTo("Java");
        assertThat(f.getSortOrder()).isEqualTo(1);
        assertThat(f.getPublished()).isTrue();
    }

    @Test
    void applyTo_反映しデフォルトを埋める() {
        TimelineEventForm f = new TimelineEventForm();
        f.setYear(2020);
        f.setTitle("入学");
        f.setSortOrder(null);
        f.setPublished(null);

        TimelineEvent e = new TimelineEvent();
        f.applyTo(e);

        assertThat(e.getYear()).isEqualTo(2020);
        assertThat(e.getTitle()).isEqualTo("入学");
        assertThat(e.getSortOrder()).isZero();
        assertThat(e.getPublished()).isFalse();
    }

    @Test
    void toEntity_新しいエンティティを作る() {
        TimelineEventForm f = new TimelineEventForm();
        f.setTitle("入学");
        assertThat(f.toEntity().getTitle()).isEqualTo("入学");
    }
}
