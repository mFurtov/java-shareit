package ru.practicum.shareit.request.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void getAllRequest() {
        User user = new User("user", "user@ya.ru");
        User userTwo = new User("use2r", "user2@ya.ru");
        entityManager.persist(user);
        assertTrue(itemRequestRepository.getAllRequest(1, Sort.by(Sort.Direction.DESC, "created")).isEmpty());

        itemRequestRepository.save(new ItemRequest("тест", user, LocalDateTime.now()));
        itemRequestRepository.save(new ItemRequest("тест2", user, LocalDateTime.now()));
        assertTrue(itemRequestRepository.getAllRequest(1, Sort.by(Sort.Direction.DESC, "created")).isEmpty());

        assertEquals(2, itemRequestRepository.getAllRequest(2, Sort.by(Sort.Direction.DESC, "created")).size());
        assertEquals("тест2", itemRequestRepository.getAllRequest(2, Sort.by(Sort.Direction.DESC, "created")).get(0).getDescription());
        assertEquals(user, itemRequestRepository.getAllRequest(2, Sort.by(Sort.Direction.DESC, "created")).get(0).getRequestor());
    }
}