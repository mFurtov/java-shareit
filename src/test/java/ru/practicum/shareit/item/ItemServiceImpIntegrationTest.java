package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.pageable.PageableCreate;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceImpIntegrationTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Test
    @Transactional
    public void getItems() {
        User owner = new User("test", "test@ya.ru");
        User requestor = new User("test2", "test2@ya.ru");
        User author = new User("test3", "test3@ya.ru");
        ItemRequest itemRequest = new ItemRequest("testRequest", requestor, LocalDateTime.now());
        Item item = new Item("Item", "item", true, owner, itemRequest);
        Item itemTwo = new Item("Item2", "item2", true, owner, itemRequest);
        Booking booking = new Booking(LocalDateTime.now().minusHours(1), LocalDateTime.now().minusMinutes(10), item, author, BookingStatus.WAITING);
        Booking bookingTwo = new Booking(LocalDateTime.now().minusMinutes(10), LocalDateTime.now().plusHours(2), item, author, BookingStatus.WAITING);
        Booking bookingThree = new Booking(LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2), item, author, BookingStatus.WAITING);
        userRepository.save(owner);
        userRepository.save(requestor);
        userRepository.save(author);
        itemRequestRepository.save(itemRequest);
        itemRepository.save(item);
        itemRepository.save(itemTwo);
        bookingRepository.save(booking);
        bookingRepository.save(bookingTwo);
        bookingRepository.save(bookingThree);

        List<ItemDto> itemList = itemService.getItems(1, PageableCreate.getPageable(0, 10, Sort.by(Sort.Direction.ASC, "id")));

        assertEquals(2, itemList.size());
        assertEquals(item.getName(), itemList.get(0).getName());
        assertEquals(itemTwo.getName(), itemList.get(1).getName());
        assertEquals(2, itemList.get(0).getLastBooking().getId());
        assertEquals(3, itemList.get(0).getNextBooking().getId());
        assertNull(itemList.get(1).getLastBooking());

    }

}