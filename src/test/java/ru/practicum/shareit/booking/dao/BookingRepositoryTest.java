package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    TestEntityManager testEntityManager;


    @Test
    @DirtiesContext
    void findByIdOrThrow() {
        assertThrows(JpaObjectRetrievalFailureException.class, () -> bookingRepository.findByIdOrThrow(1));

        User user = new User("user", "user@ya.ru");
        Item item = new Item("Дрель", "Простая дрель", true, user, null);
        bookingRepository.save(new Booking(LocalDateTime.now().minusHours(1), LocalDateTime.MAX, item, user, BookingStatus.APPROVED));
        assertDoesNotThrow(() -> bookingRepository.findByIdOrThrow(1));
    }

    @Test
    @DirtiesContext
    void searchBooking() {
        assertNull(bookingRepository.searchBooking(1, 1));

        User user = new User("user", "user@ya.ru");
        User userTwo = new User("user2", "user2@ya.ru");
        Item item = new Item("Дрель", "Простая дрель", true, userTwo, null);
        Item itemTwo = new Item("Дрель2", "Простая дрель2", true, userTwo, null);
        testEntityManager.persist(user);
        testEntityManager.persist(userTwo);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);
        bookingRepository.save(new Booking(LocalDateTime.now(), LocalDateTime.MAX, item, user, BookingStatus.APPROVED));

        Booking booking = bookingRepository.searchBooking(2, 1);
        assertNotNull(booking);
        assertEquals(booking.getItem().getOwner(), userTwo);


        Booking bookingTwo = bookingRepository.searchBooking(1, 1);
        assertNotNull(bookingTwo);
        assertEquals(bookingTwo.getBooker(), user);

    }

    @Test
    @DirtiesContext
    void searchBookingOrThrow() {
        assertThrows(JpaObjectRetrievalFailureException.class, () -> bookingRepository.searchBookingOrThrow(1, 1));

        User user = new User("user", "user@ya.ru");
        User userTwo = new User("user2", "user2@ya.ru");
        Item item = new Item("Дрель", "Простая дрель", true, userTwo, null);
        Item itemTwo = new Item("Дрель2", "Простая дрель2", true, userTwo, null);
        testEntityManager.persist(user);
        testEntityManager.persist(userTwo);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);
        bookingRepository.save(new Booking(LocalDateTime.now(), LocalDateTime.MAX, item, user, BookingStatus.APPROVED));
        assertDoesNotThrow(() -> bookingRepository.searchBookingOrThrow(2, 1));
    }

    @Test
    @DirtiesContext
    void findCurrentBookings() {
        User user = new User("user", "user@ya.ru");
        User userTwo = new User("user2", "user2@ya.ru");
        Item item = new Item("Дрель", "Простая дрель", true, userTwo, null);
        Item itemTwo = new Item("Дрель2", "Простая дрель2", true, userTwo, null);
        testEntityManager.persist(user);
        testEntityManager.persist(userTwo);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);

        List<Booking> bookings = bookingRepository.findCurrentBookings(LocalDateTime.now(), 1, PageRequest.of(0, 10));
        assertEquals(0, bookings.size());

        bookingRepository.save(new Booking(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1), item, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), itemTwo, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), itemTwo, user, BookingStatus.APPROVED));

        List<Booking> bookingsTwo = bookingRepository.findCurrentBookings(LocalDateTime.now(), 1, PageRequest.of(0, 10));
        List<Booking> bookings1 = bookingRepository.findAll();
        assertEquals(1, bookingsTwo.size());
        assertEquals(1, bookingsTwo.get(0).getId());
        assertEquals(item, bookingsTwo.get(0).getItem());

        assertTrue(bookingRepository.findCurrentBookings(LocalDateTime.now(), 2, PageRequest.of(0, 10)).isEmpty());
    }

    @Test
    @DirtiesContext
    void findPastBookings() {
        User user = new User("user", "user@ya.ru");
        User userTwo = new User("user2", "user2@ya.ru");
        Item item = new Item("Дрель", "Простая дрель", true, userTwo, null);
        Item itemTwo = new Item("Дрель2", "Простая дрель2", true, userTwo, null);
        testEntityManager.persist(user);
        testEntityManager.persist(userTwo);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);

        List<Booking> bookings = bookingRepository.findPastBookings(LocalDateTime.now(), 1, PageRequest.of(0, 10));
        assertEquals(0, bookings.size());

        bookingRepository.save(new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), itemTwo, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), itemTwo, user, BookingStatus.APPROVED));

        List<Booking> bookingsTwo = bookingRepository.findPastBookings(LocalDateTime.now(), 1, PageRequest.of(0, 10));

        assertEquals(1, bookingsTwo.size());
        assertEquals(3, bookingsTwo.get(0).getId());
        assertEquals(1, bookingsTwo.get(0).getBooker().getId());
    }

    @Test
    @DirtiesContext
    void findFutureBookings() {
        User user = new User("user", "user@ya.ru");
        User userTwo = new User("user2", "user2@ya.ru");
        Item item = new Item("Дрель", "Простая дрель", true, userTwo, null);
        Item itemTwo = new Item("Дрель2", "Простая дрель2", true, userTwo, null);
        testEntityManager.persist(user);
        testEntityManager.persist(userTwo);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);

        List<Booking> bookings = bookingRepository.findFutureBookings(LocalDateTime.now(), 1, PageRequest.of(0, 10));
        assertEquals(0, bookings.size());

        bookingRepository.save(new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), itemTwo, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), itemTwo, user, BookingStatus.APPROVED));

        List<Booking> bookingsTwo = bookingRepository.findFutureBookings(LocalDateTime.now(), 1, PageRequest.of(0, 10));
        assertEquals(1, bookingsTwo.size());
        assertEquals(2, bookingsTwo.get(0).getId());
        assertEquals(1, bookingsTwo.get(0).getBooker().getId());
    }


    @Test
    @DirtiesContext
    void findCurrentBookingsOwner() {
        User user = new User("user", "user@ya.ru");
        User userTwo = new User("user2", "user2@ya.ru");
        Item item = new Item("Дрель", "Простая дрель", true, userTwo, null);
        Item itemTwo = new Item("Дрель2", "Простая дрель2", true, userTwo, null);
        testEntityManager.persist(user);
        testEntityManager.persist(userTwo);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);

        List<Booking> bookings = bookingRepository.findCurrentBookingsOwner(LocalDateTime.now(), 1, PageRequest.of(0, 10));
        assertEquals(0, bookings.size());

        bookingRepository.save(new Booking(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1), item, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), itemTwo, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), itemTwo, user, BookingStatus.APPROVED));

        List<Booking> bookingsTwo = bookingRepository.findCurrentBookingsOwner(LocalDateTime.now(), 2, PageRequest.of(0, 10));
        assertEquals(1, bookingsTwo.size());
        assertEquals(1, bookingsTwo.get(0).getId());
        assertEquals(item, bookingsTwo.get(0).getItem());

        assertTrue(bookingRepository.findCurrentBookingsOwner(LocalDateTime.now(), 1, PageRequest.of(0, 10)).isEmpty());

    }


    @Test
    @DirtiesContext
    void findPastBookingsOwner() {
        User user = new User("user", "user@ya.ru");
        User userTwo = new User("user2", "user2@ya.ru");
        Item item = new Item("Дрель", "Простая дрель", true, userTwo, null);
        Item itemTwo = new Item("Дрель2", "Простая дрель2", true, userTwo, null);
        testEntityManager.persist(user);
        testEntityManager.persist(userTwo);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);

        List<Booking> bookings = bookingRepository.findPastBookingsOwner(LocalDateTime.now(), 1, PageRequest.of(0, 10));
        assertEquals(0, bookings.size());

        bookingRepository.save(new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), itemTwo, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), itemTwo, user, BookingStatus.APPROVED));

        List<Booking> bookingsTwo = bookingRepository.findPastBookingsOwner(LocalDateTime.now(), 2, PageRequest.of(0, 10));

        assertEquals(1, bookingsTwo.size());
        assertEquals(3, bookingsTwo.get(0).getId());
        assertEquals(1, bookingsTwo.get(0).getBooker().getId());
    }

    @Test
    @DirtiesContext
    void findFutureBookingsOwner() {
        User user = new User("user", "user@ya.ru");
        User userTwo = new User("user2", "user2@ya.ru");
        Item item = new Item("Дрель", "Простая дрель", true, userTwo, null);
        Item itemTwo = new Item("Дрель2", "Простая дрель2", true, userTwo, null);
        testEntityManager.persist(user);
        testEntityManager.persist(userTwo);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);

        List<Booking> bookings = bookingRepository.findFutureBookingsOwner(LocalDateTime.now(), 1, PageRequest.of(0, 10));
        assertEquals(0, bookings.size());

        bookingRepository.save(new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), itemTwo, user, BookingStatus.APPROVED));
        bookingRepository.save(new Booking(LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), itemTwo, user, BookingStatus.APPROVED));

        List<Booking> bookingsTwo = bookingRepository.findFutureBookingsOwner(LocalDateTime.now(), 2, PageRequest.of(0, 10));
        assertEquals(1, bookingsTwo.size());
        assertEquals(2, bookingsTwo.get(0).getId());
        assertEquals(1, bookingsTwo.get(0).getBooker().getId());

    }

}