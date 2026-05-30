package com.kondo.portfolio.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.ContactMessage;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class ContactMessageRepositoryTest {

    @Autowired private ContactMessageRepository repository;
    @Autowired private TestEntityManager em;

    private ContactMessage msg(String name, LocalDateTime createdAt) {
        ContactMessage m = new ContactMessage();
        m.setName(name);
        m.setEmail(name + "@example.com");
        m.setMessage("本文");
        m.setCreatedAt(createdAt);
        return m;
    }

    @Test
    void 新しい順で返す() {
        em.persist(msg("古い", LocalDateTime.of(2026, 1, 1, 0, 0)));
        em.persist(msg("新しい", LocalDateTime.of(2026, 5, 1, 0, 0)));
        em.persist(msg("中間", LocalDateTime.of(2026, 3, 1, 0, 0)));
        em.flush();

        List<ContactMessage> result = repository.findAllByOrderByCreatedAtDesc();

        assertThat(result).extracting(ContactMessage::getName).containsExactly("新しい", "中間", "古い");
    }

    @Test
    void 件数ゼロなら空リスト() {
        assertThat(repository.findAllByOrderByCreatedAtDesc()).isEmpty();
    }
}
