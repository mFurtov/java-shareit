package ru.practicum.shareit.item.itemDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void search() {
        User user = new User("user", "user@ya.ru");
        testEntityManager.persist(user);
        itemRepository.save(new Item("Дрель", "Простая дрель", true, user, null));
        assertTrue(itemRepository.search("тест", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).isEmpty());
        assertEquals(1, itemRepository.search("дре", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).size());
        assertEquals("Дрель", itemRepository.search("дре", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).get(0).getName());
        assertEquals(1, itemRepository.search("простая", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).size());
        assertEquals("Простая дрель", itemRepository.search("простая", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).get(0).getDescription());

        Item item = itemRepository.getById(1);
        item.setAvailable(false);
        itemRepository.save(item);
        assertTrue(itemRepository.search("тест", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).isEmpty());
        assertEquals(0, itemRepository.search("дре", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).size());
        assertEquals(0, itemRepository.search("простая", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).size());

    }

}