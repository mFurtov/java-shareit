package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext
class ItemRequestServiceImplTest {
    private ItemRequestService itemRequestService;
    @Mock
    ItemRequestRepository repository;
    @Mock
    UserService userService;
    @Mock
    ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        itemRequestService = new ItemRequestServiceImpl(repository, userService, itemRepository);
    }

    @Test
    void postRequest() {
        User user = new User(1, "test", "test@ya.ru");
        UserDto userDto = new UserDto(1, "test", "test@ya.ru");
        ItemRequest itemRequest = new ItemRequest("test", user, LocalDateTime.now());
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto();
        itemRequestCreateDto.setDescription("testDescription");
        ArgumentCaptor<ItemRequest> captor = ArgumentCaptor.forClass(ItemRequest.class);

        when(userService.getUserById(anyInt())).thenReturn(userDto);
        when(repository.save(captor.capture())).thenReturn(itemRequest);
        itemRequestService.postRequest(1, itemRequestCreateDto);

        verify(repository, times(1)).save(any());

        ItemRequest capturedItemRequest = captor.getValue();
        assertEquals(user, capturedItemRequest.getRequestor());
        assertEquals("testDescription", capturedItemRequest.getDescription());
    }

    @Test
    void getAllRequest() {
        User user = new User(1, "test", "test@ya.ru");
        ItemRequest itemRequest = new ItemRequest("test", user, LocalDateTime.now());
        Optional<ItemRequest> optionalEmpty = Optional.empty();

        when(repository.findById(1)).thenReturn(Optional.of(itemRequest));
        when(repository.findById(2)).thenReturn(optionalEmpty);

        assertEquals(itemRequest.getDescription(), itemRequestService.getAllRequest(1).getDescription());
        assertNull(itemRequestService.getAllRequest(2));
    }

    @Test
    void getRequest() {
        User user = new User(1, "test", "test@ya.ru");
        UserDto userDto = new UserDto(1, "test", "test@ya.ru");
        ItemRequest itemRequest = new ItemRequest("itemRequest 1", user, LocalDateTime.now());
        ItemRequest itemRequestTwo = new ItemRequest("itemRequest 2 ", user, LocalDateTime.now().plusHours(1));
        itemRequest.setId(1);
        itemRequestTwo.setId(2);
        List<ItemRequest> itemRequests = List.of(itemRequest, itemRequestTwo);
        User owner = new User(1, "Owner", "owner@ya.ru");
        User ownerTwo = new User(1, "Owner", "owner@ya.ru");
        Item item = new Item(1, "Item 1", "item", true, owner, itemRequestTwo);
        Item itemTwo = new Item(2, "Item 2", "item", true, ownerTwo, itemRequestTwo);
        List<Item> itemList = List.of(item, itemTwo);

        when(userService.getUserById(anyInt())).thenReturn(userDto);
        when(itemRepository.findByRequestIdIn(anyList())).thenReturn(itemList);
        when(repository.findByRequestorId(anyInt())).thenReturn(itemRequests);

        List<ItemRequestDto> request = itemRequestService.getRequest(1);

        assertEquals(0, request.get(1).getItems().size());
        assertEquals(2, request.get(0).getItems().size());
        assertEquals("Item 1", request.get(0).getItems().get(0).getName());
        assertEquals(2, request.size());
        assertEquals(2, request.get(0).getId());

    }

    @Test
    void getAllRequestPage() {
        User user = new User(2, "test", "test@ya.ru");
        UserDto userDto = new UserDto(2, "test", "test@ya.ru");
        ItemRequest itemRequest = new ItemRequest("itemRequest 1", user, LocalDateTime.now());
        ItemRequest itemRequestTwo = new ItemRequest("itemRequest 2 ", user, LocalDateTime.now().plusHours(1));
        itemRequest.setId(1);
        itemRequestTwo.setId(2);
        List<ItemRequest> itemRequests = List.of(itemRequest, itemRequestTwo);
        User owner = new User(1, "Owner", "owner@ya.ru");
        User ownerTwo = new User(1, "Owner", "owner@ya.ru");
        Item item = new Item(1, "Item 1", "item", true, owner, itemRequestTwo);
        Item itemTwo = new Item(2, "Item 2", "item", true, ownerTwo, itemRequestTwo);
        List<Item> itemList = List.of(item, itemTwo);

        when(userService.getUserById(anyInt())).thenReturn(userDto);
        when(itemRepository.findByRequestIdIn(anyList())).thenReturn(itemList);
        when(repository.getAllRequest(anyInt(), any(Sort.class))).thenReturn(itemRequests);

        List<ItemRequestDto> request = itemRequestService.getAllRequestPage(1, 0, 10);
        assertEquals(0, request.get(1).getItems().size());
        assertEquals(2, request.get(0).getItems().size());
        assertEquals("Item 1", request.get(0).getItems().get(0).getName());
        assertEquals(2, request.size());
        assertEquals(2, request.get(0).getId());

    }

    @Test
    void getRequestById() {
        User user = new User(2, "test", "test@ya.ru");
        UserDto userDto = new UserDto(2, "test", "test@ya.ru");
        ItemRequest itemRequest = new ItemRequest("itemRequest 1", user, LocalDateTime.now());
        itemRequest.setId(1);
        User owner = new User(1, "Owner", "owner@ya.ru");
        User ownerTwo = new User(1, "Owner", "owner@ya.ru");
        Item item = new Item(1, "Item 1", "item", true, owner, itemRequest);
        Item itemTwo = new Item(2, "Item 2", "item", true, ownerTwo, itemRequest);
        List<Item> itemList = List.of(item, itemTwo);

        when(userService.getUserById(anyInt())).thenReturn(userDto);
        when(itemRepository.findByRequestIdIn(anyList())).thenReturn(itemList);
        when(repository.getById(anyInt())).thenReturn(itemRequest);

        ItemRequestDto request = itemRequestService.getRequestById(1, 1);
        assertEquals(2, request.getItems().size());
        assertEquals("itemRequest 1", request.getDescription());
        assertEquals(item.getName(), request.getItems().get(0).getName());

    }
}