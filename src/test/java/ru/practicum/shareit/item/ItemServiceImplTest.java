package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemDao.CommentRepository;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.pageable.PageableCreate;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext
class ItemServiceImplTest {

    private ItemService itemService;
    @Mock
    UserService userService;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    ItemRequestService itemRequestService;

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceImpl(userService, itemRepository, bookingRepository, commentRepository, itemRequestService);
    }

    @Test
    void getItemId() {
        User owner = new User(1, "Owner", "owner@ya.ru");
        User author = new User(2, "author", "author@ya.ru");
        Item item = new Item(1, "Item", "item", true, owner, null);
        User booker = new User(2, "Booker", "booker@ya.ru");
        Booking booking = new Booking(1, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1), item, booker, BookingStatus.WAITING);
        Booking bookingTwo = new Booking(2, LocalDateTime.now().plusHours(1), LocalDateTime.now().minusHours(2), item, booker, BookingStatus.WAITING);
        Booking bookingThree = new Booking(3, LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusHours(2), item, booker, BookingStatus.WAITING);
        List<Booking> bookingList = List.of(booking, bookingTwo, bookingThree);
        Comment comment = new Comment("test", item, author);
        List<Comment> commentList = List.of(comment);
        when(itemRepository.getById(anyInt())).thenReturn(item);
        when(bookingRepository.findByItemIdAndItemOwnerIdAndStatusNotOrderByStart(anyInt(), anyInt(), any(BookingStatus.class))).thenReturn(bookingList);
        when(commentRepository.findByItemId(anyInt())).thenReturn(commentList);
        ItemDto itemDto = itemService.getItemId(1, 1);
        assertEquals(booking.getId(), itemService.getItemId(1, 1).getLastBooking().getId());
        assertEquals(booking.getStart(), itemService.getItemId(1, 1).getLastBooking().getStart());
        assertEquals(booking.getEnd(), itemService.getItemId(1, 1).getLastBooking().getEnd());
        assertEquals(bookingThree.getId(), itemService.getItemId(1, 1).getNextBooking().getId());
        assertEquals(bookingThree.getStart(), itemService.getItemId(1, 1).getNextBooking().getStart());
        assertEquals(bookingThree.getEnd(), itemService.getItemId(1, 1).getNextBooking().getEnd());
        assertEquals(commentList.get(0).getAuthor().getName(), itemService.getItemId(1, 1).getComments().get(0).getAuthorName());
        assertEquals(commentList.get(0).getText(), itemService.getItemId(1, 1).getComments().get(0).getText());


    }

    @Test
    void postItem() {
        UserDto userDto = new UserDto(1, "test", "test@ya.ru");
        User userRequestAuthor = new User(2, "userRequestAuthor", "userRequestAuthor@ya.ru");
        ItemRequest itemRequest = new ItemRequest("test description", userRequestAuthor, LocalDateTime.now());
        User owner = new User(1, "Owner", "owner@ya.ru");
        Item item = new Item(1, "Item", "item", true, owner, itemRequest);
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("description");
        itemCreateDto.setName("name");
        itemCreateDto.setRequestId(1);

        when(userService.getUserById(anyInt())).thenReturn(userDto);
        when(itemRequestService.getAllRequest(anyInt())).thenReturn(itemRequest);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        itemService.postItem(1, itemCreateDto);
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void patchItem() {
        User userRequestAuthor = new User(2, "userRequestAuthor", "userRequestAuthor@ya.ru");
        ItemRequest itemRequest = new ItemRequest("test description", userRequestAuthor, LocalDateTime.now());
        User owner = new User(1, "Owner", "owner@ya.ru");
        Item item = new Item(1, "Item", "item", true, owner, itemRequest);
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("nameUpdate");
        ItemCreateDto itemCreateDtoTwo = new ItemCreateDto();
        itemCreateDtoTwo.setDescription("descriptionUpdate");
        ItemCreateDto itemCreateDtoThree = new ItemCreateDto();
        itemCreateDtoThree.setAvailable(false);

        when(itemRepository.getById(anyInt())).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        assertEquals("nameUpdate", itemService.patchItem(1, 1, itemCreateDto).getName());
        assertEquals("descriptionUpdate", itemService.patchItem(1, 1, itemCreateDtoTwo).getDescription());
        assertEquals(false, itemService.patchItem(1, 1, itemCreateDtoThree).getAvailable());

    }

    @Test
    void searchItems() {
        Pageable pageable = mock(Pageable.class);
        itemService.searchItems("test", pageable);
        verify(itemRepository, times(1)).search("test", pageable);
    }

    @Test
    void postComments() {
        User owner = new User(1, "Owner", "owner@ya.ru");
        Item item = new Item(1, "Item", "item", true, owner, null);
        UserDto userDto = new UserDto(1, "test", "test@ya.ru");
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("test");
        User author = new User(2, "author", "author@ya.ru");
        Comment comment = new Comment("test", item, author);

        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(anyInt(), anyInt(), any(BookingStatus.class), any(LocalDateTime.class))).thenReturn(false);
        when(userService.getUserById(anyInt())).thenReturn(userDto);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        assertThrows(ValidException.class, () -> itemService.postComments(2, 1, commentRequestDto));
        assertThrows(ValidException.class, () -> itemService.postComments(1, 2, commentRequestDto));

        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(eq(1), eq(userDto.getId()), eq(BookingStatus.APPROVED), any(LocalDateTime.class))).thenReturn(true);
        assertEquals("test", itemService.postComments(1, 1, commentRequestDto).getText());

    }

    @Test
    void getItems() {
        User owner = new User(1, "Owner", "owner@ya.ru");
        Item item = new Item(1, "Item", "item", true, owner, null);
        User ownerTwo = new User(2, "Owner", "owner@ya.ru");
        Item itemTwo = new Item(2, "Item2", "item2", true, owner, null);
        List<Item> itemList = List.of(item, itemTwo);
        User author = new User(3, "author", "author@ya.ru");
        Comment comment = new Comment("test", item, author);
        comment.setId(1);
        Comment commentTwo = new Comment("test2", itemTwo, author);
        commentTwo.setId(2);
        List<Comment> commentList = List.of(comment, commentTwo);
        User booker = new User(2, "Booker", "booker@ya.ru");
        Booking booking = new Booking(1, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1), item, booker, BookingStatus.WAITING);
        Booking bookingTwo = new Booking(2, LocalDateTime.now().plusHours(1), LocalDateTime.now().minusHours(2), item, booker, BookingStatus.WAITING);
        Booking bookingThree = new Booking(3, LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusHours(2), item, booker, BookingStatus.WAITING);
        Booking bookingFour = new Booking(4, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(2), itemTwo, booker, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking, bookingTwo, bookingThree, bookingFour);

        when(itemRepository.findAllByOwnerId(anyInt(), any(Pageable.class))).thenReturn(itemList);
        when(bookingRepository.findByItemIn(anyList())).thenReturn(bookings);
        when(commentRepository.findByItemIn(anyList(), any(Sort.class))).thenReturn(commentList);

        List<ItemDto> itemDtoList = itemService.getItems(1, PageableCreate.getPageable(0, 10, Sort.by(Sort.Direction.ASC, "id")));
        assertEquals("Item", itemService.getItems(1, PageableCreate.getPageable(0, 10, Sort.by(Sort.Direction.ASC, "id"))).get(0).getName());
        assertEquals("Item2", itemService.getItems(1, PageableCreate.getPageable(0, 10, Sort.by(Sort.Direction.ASC, "id"))).get(1).getName());
        assertEquals(booking.getId(), itemService.getItems(1, PageableCreate.getPageable(0, 10, Sort.by(Sort.Direction.ASC, "id"))).get(0).getLastBooking().getId());
        assertEquals(bookingThree.getId(), itemService.getItems(1, PageableCreate.getPageable(0, 10, Sort.by(Sort.Direction.ASC, "id"))).get(0).getNextBooking().getId());
        assertEquals(bookingFour.getId(), itemService.getItems(1, PageableCreate.getPageable(0, 10, Sort.by(Sort.Direction.ASC, "id"))).get(1).getLastBooking().getId());
        assertEquals(comment.getText(), itemService.getItems(1, PageableCreate.getPageable(0, 10, Sort.by(Sort.Direction.ASC, "id"))).get(0).getComments().get(0).getText());
        assertEquals(commentTwo.getText(), itemService.getItems(1, PageableCreate.getPageable(0, 10, Sort.by(Sort.Direction.ASC, "id"))).get(1).getComments().get(0).getText());

    }
}