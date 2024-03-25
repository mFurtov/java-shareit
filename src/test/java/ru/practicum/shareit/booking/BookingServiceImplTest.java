package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingService bookingService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemService itemService;
    @Mock
    UserService userService;

    @BeforeEach
    public void startUp() {
        bookingService = new BookingServiceImpl(bookingRepository, itemService, userService);
    }

    @Test
    void postBooking() {
        User owner = new User(1, "Owner", "owner@ya.ru");
        UserDto ownerDto = new UserDto(1, "Owner", "owner@ya.ru");
        Item item = new Item(1, "Item", "item", true, owner, null);
        Item itemTwo = new Item(2, "ItemTwo", "itemTwo", false, owner, null);
        User booker = new User(2, "Booker", "booker@ya.ru");
        UserDto bookerDto = new UserDto(2, "Booker", "booker@ya.ru");
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        BookingDtoRequest bookingDtoRequestTwo = new BookingDtoRequest(2, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, booker, BookingStatus.WAITING);

        when(userService.getUserById(1)).thenReturn(ownerDto);
        when(userService.getUserById(2)).thenReturn(bookerDto);
        when(itemService.getItemNDto(1)).thenReturn(item);
        when(itemService.getItemNDto(2)).thenReturn(itemTwo);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);


        assertThrows(OwnerException.class, () -> bookingService.postBooking(1, bookingDtoRequest));
        assertThrows(ValidException.class, () -> bookingService.postBooking(2, bookingDtoRequestTwo));

        bookingService.postBooking(2, bookingDtoRequest);
        verify(bookingRepository, times(1)).save(any(Booking.class));

        assertEquals(item.getName(), bookingService.postBooking(2, bookingDtoRequest).getItem().getName());
        assertEquals(item.getDescription(), bookingService.postBooking(2, bookingDtoRequest).getItem().getDescription());

    }

    @Test
    void patchBookingApproved() {
        User owner = new User(1, "Owner", "owner@ya.ru");
        Item item = new Item(1, "Item", "item", true, owner, null);
        User booker = new User(2, "Booker", "booker@ya.ru");
        Booking booking = new Booking(1, LocalDateTime.of(2024, 01, 01, 12, 12), LocalDateTime.of(2024, 01, 02, 12, 12), item, booker, BookingStatus.APPROVED);
        Booking bookingTwo = new Booking(1, LocalDateTime.of(2024, 01, 01, 12, 12), LocalDateTime.of(2024, 01, 02, 12, 12), item, booker, BookingStatus.WAITING);

        when(bookingRepository.getById(1)).thenReturn(booking);
        when(bookingRepository.getById(2)).thenReturn(bookingTwo);
        when(bookingRepository.save(bookingTwo)).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            assertEquals(BookingStatus.APPROVED, savedBooking.getStatus());
            return savedBooking;
        });

        assertThrows(ValidException.class, () -> bookingService.patchBookingApproved(1, 1, true));
        assertThrows(OwnerException.class, () -> bookingService.patchBookingApproved(2, 2, true));

        assertEquals("booker@ya.ru", bookingService.patchBookingApproved(1, 2, true).getBooker().getEmail());
        assertEquals("booker@ya.ru", bookingService.patchBookingApproved(1, 2, false).getBooker().getEmail());


    }

    @Test
    void getBooking() {
    }

    @Test
    void getBookingWithStatus() {
    }

    @Test
    void getBookingWithStatusOwner() {
    }
}