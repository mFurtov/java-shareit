package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFromItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository repository;
    private final BookingRepository bookingRepository;

    //    @Override
//    public List<ItemDto> getItem(int id) {
//        List<Item> item = repository.findByOwnerIdOrderById(id);
//        return ItemMapper.mapToListItemDto(item);
//    }
    @Override
    public List<ItemDto> getItem(int userId) {
        List<Item> item = repository.findByOwnerIdOrderById(userId);
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item i : item) {
            itemDto.add(addBookingInfo(i, userId, i.getId()));
        }
        return itemDto;
    }

    @Override
    public ItemDto getItemId(int userId, int id) {
        Item item = repository.getById(id);
        ItemDto itemDto = addBookingInfo(item, userId, id);
        return itemDto;
    }

    private ItemDto addBookingInfo(Item item, int userId, int id) {
        ItemDto itemDto = ItemMapper.maToItemDto(item);
        List<Booking> bookings = bookingRepository.findByItemIdOrderByStart(id);
        bookings.removeIf(booking -> booking.getItem().getOwner().getId() != userId);
        bookings.removeIf(booking -> booking.getStatus() == BookingStatus.REJECTED);
        if (!bookings.isEmpty()) {
            itemDto.setLastBooking(BookingMapper.mapToBookingDtoFromItem(bookings.get(0)));
            if (bookings.size() > 1) {
                itemDto.setNextBooking(BookingMapper.mapToBookingDtoFromItem(bookings.get(1)));
            } else {
                itemDto.setNextBooking(null);
            }
        } else {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }
        return itemDto;
    }


    @Override
    public Item getItemNDto(int id) {
        try {
            Item item = repository.getById(id);
            item.toString();
            return item;
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("Искомый обьект не найден");
        }
    }

    @Override
    public ItemDto postItem(int userId, ItemDto itemDto) {
        User user = UserMapper.fromUserDto(userService.getUserById(userId));
        return ItemMapper.maToItemDto(repository.save(ItemMapper.mapFromItemDto(itemDto, user)));
    }

    @Override
    public ItemDto patchItem(int userId, int id, ItemDto itemDto) {
        Item item = repository.getById(id);

        if (item.getOwner().getId() == userId) {
            if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
        } else {
            log.error("Указанный владелец не владелец вещи");
            throw new OwnerException("Доступ к предмету отсутствует, указан неверный владелец");
        }

        return ItemMapper.maToItemDto(repository.save(item));
    }

    public List<ItemDto> searchItems(String search) {
        return ItemMapper.mapToListItemDto(repository.search(search));
    }

    public void setAvailable(Item item) {
        item.setAvailable(false);
        repository.save(item);
    }
}
