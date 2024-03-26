package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class BookingServiceImplIntegrationTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void patchBookingApproved() {
        User user = new User("Test", "test@ya.ru");
        User userTwo = new User("Test2", "test2@ya.ru");
        Item item = new Item("testItem", "descriptionTest", true, user, null);
        User booker = new User("Test3", "test3@ya.ru");
        userRepository.save(user);
        userRepository.save(userTwo);
        userRepository.save(booker);
        itemRepository.save(item);
        Booking booking = bookingRepository.save(new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, booker, BookingStatus.WAITING));
        Booking bookingTwo = bookingRepository.save(new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, booker, BookingStatus.APPROVED));


        assertThrows(OwnerException.class, () -> bookingService.patchBookingApproved(2, 1, true));
        assertThrows(EntityNotFoundException.class, () -> bookingService.patchBookingApproved(1, 3, true));
        assertEquals(BookingStatus.REJECTED, bookingService.patchBookingApproved(1, 1, false).getStatus());
        assertEquals(BookingStatus.APPROVED, bookingService.patchBookingApproved(1, 1, true).getStatus());
        assertThrows(ValidException.class, () -> bookingService.patchBookingApproved(1, 2, true));

    }

}