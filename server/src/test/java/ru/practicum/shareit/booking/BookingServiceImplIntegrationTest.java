package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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
@DirtiesContext
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
    @Transactional
    @DirtiesContext
    public void patchBookingApproved() {
        User user = new User("Test", "test@ya.ru");
        Item item = new Item("testItem", "descriptionTest", true, user, null);
        User booker = new User("Test3", "test3@ya.ru");
        Booking booking = new Booking(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(1), item, booker, BookingStatus.WAITING);
        Booking bookingTwo = new Booking(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(2), item, booker, BookingStatus.APPROVED);
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(bookingTwo);

        assertThrows(EntityNotFoundException.class, () -> bookingService.patchBookingApproved(1, 3, true));
        assertThrows(OwnerException.class, () -> bookingService.patchBookingApproved(2, 1, true));
        assertEquals(BookingStatus.REJECTED, bookingService.patchBookingApproved(1, 1, false).getStatus());
        assertEquals(BookingStatus.APPROVED, bookingService.patchBookingApproved(1, 1, true).getStatus());
        assertThrows(ValidException.class, () -> bookingService.patchBookingApproved(1, 2, true));


    }

}