package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRequestServiceImplIntegrationTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void getAllRequestPage() {
        User user = new User("test", "test@ya.ru");
        User userSearch = new User("test2", "test@ya.ru");
        ItemRequest itemRequest = new ItemRequest("itemRequest 1", user, LocalDateTime.now());
        ItemRequest itemRequestTwo = new ItemRequest("itemRequest 2", user, LocalDateTime.now().plusHours(1));
        ItemRequest itemRequestThree = new ItemRequest("itemRequest 3", user, LocalDateTime.now().plusHours(1));
        ItemRequest itemRequestFour = new ItemRequest("itemRequest 4", user, LocalDateTime.now().plusHours(1));
        User owner = new User("Owner", "owner@ya.ru");
        User ownerTwo = new User("Owner2", "owner2@ya.ru");
        Item item = new Item(1, "Item 1", "item", true, owner, itemRequestTwo);
        Item itemTwo = new Item(2, "Item 2", "item", true, ownerTwo, itemRequestFour);

        userRepository.save(user);
        userRepository.save(owner);
        userRepository.save(ownerTwo);
        itemRequestRepository.save(itemRequest);
        itemRequestRepository.save(itemRequestTwo);
        itemRequestRepository.save(itemRequestThree);
        itemRequestRepository.save(itemRequestFour);
        itemRepository.save(item);
        itemRepository.save(itemTwo);

        List<ItemRequestDto> request = itemRequestService.getAllRequestPage(2, 0, 10);

        assertEquals(4, request.size());
        assertEquals(4, request.get(0).getId());
        assertEquals(1, request.get(3).getId());
        assertEquals(itemTwo.getName(), request.get(0).getItems().get(0).getName());
        assertEquals(item.getName(), request.get(2).getItems().get(0).getName());

        List<ItemRequestDto> requestTwo = itemRequestService.getAllRequestPage(2, 0, 1);
        assertEquals(1, requestTwo.size());
        assertEquals(4, request.get(0).getId());
    }

}