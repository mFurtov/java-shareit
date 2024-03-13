package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemDao.CommentRepository;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private LocalDateTime dateTime = null;


    @Override
    public List<ItemDto> getItem(int userId) {
        List<Item> item = itemRepository.findByOwnerIdOrderById(userId);
        return addInfo(item);
    }

    @Override
    public ItemDto getItemId(int userId, int id) {
        Item item = itemRepository.getById(id);
        ItemDto itemDto = addInfo(item, userId, id);
        return itemDto;
    }

    private ItemDto addInfo(Item item, int userId, int id) {
        ItemDto itemDto = ItemMapper.maToItemDto(item);
        dateTime = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findByItemIdAndItemOwnerIdAndStatusNotOrderByStart(id, userId, BookingStatus.REJECTED);
        if (!bookings.isEmpty()) {
            bookings.stream().filter(b -> b.getStart().isBefore(dateTime)).max(Comparator.comparing(Booking::getStart)).ifPresent(lastBooking -> itemDto.setLastBooking(BookingMapper.mapToBookingDtoFromItem(lastBooking)));
            bookings.stream().filter(b -> b.getStart().isAfter(dateTime)).min(Comparator.comparing(Booking::getStart)).ifPresent(nextBooking -> itemDto.setNextBooking(BookingMapper.mapToBookingDtoFromItem(nextBooking)));
        }
        itemDto.setComments(CommentMapper.mapToListCommentDto(commentRepository.findByItemId(id)));
        return itemDto;
    }

    private List<ItemDto> addInfo(List<Item> items) {
        List<ItemDto> itemDtoList = ItemMapper.mapToListItemDto(items);
        List<Booking> allBookings = bookingRepository.findByItemIn(items);
        Map<ItemDto, List<CommentDto>> comments = commentRepository.findByItemIn(items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(item -> ItemMapper.maToItemDto(item.getItem()), Collectors.mapping(CommentMapper::mapToCommentDto, Collectors.toList())));

        Map<ItemDto, List<Booking>> bookingMap = new HashMap<>();
        for (ItemDto itemDto : itemDtoList) {
            List<Booking> bookingsForItem = allBookings.stream().filter(booking -> booking.getItem().getId() == itemDto.getId()).collect(Collectors.toList());
            bookingMap.put(itemDto, bookingsForItem);
            bookingsForItem.stream()
                    .filter(b -> b.getStart().isBefore(dateTime))
                    .max(Comparator.comparing(Booking::getStart))
                    .ifPresent(lastBooking -> itemDto.setLastBooking(BookingMapper.mapToBookingDtoFromItem(lastBooking)));

            bookingsForItem.stream()
                    .filter(b -> b.getStart().isAfter(dateTime))
                    .min(Comparator.comparing(Booking::getStart))
                    .ifPresent(nextBooking -> itemDto.setNextBooking(BookingMapper.mapToBookingDtoFromItem(nextBooking)));

            List<CommentDto> commentsForItem = comments.get(itemDto);
            if (commentsForItem != null) {
                itemDto.setComments(commentsForItem);
            }
        }

        return itemDtoList;
    }


    @Override
    public Item getItemNDto(int id) {
        try {
            Item item = itemRepository.getById(id);
            item.toString();
            return item;
        } catch (EntityNotFoundException ex) {
            log.error("Искомый обьект не найден");
            throw new EntityNotFoundException("Искомый обьект не найден");
        }
    }

    @Override
    public ItemDto postItem(int userId, ItemCreateDto itemDto) {
        User user = UserMapper.fromUserDto(userService.getUserById(userId));
        return ItemMapper.maToItemDto(itemRepository.save(ItemMapper.mapFromItemDto(itemDto, user)));
    }

    @Override
    public ItemDto patchItem(int userId, int id, ItemCreateDto itemDto) {
        Item item = itemRepository.getById(id);

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

        return ItemMapper.maToItemDto(itemRepository.save(item));
    }

    public List<ItemDto> searchItems(String search) {
        return ItemMapper.mapToListItemDto(itemRepository.search(search));
    }


    public CommentDto postComments(int userId, int id, CommentRequestDto commentRequestDto) {
        Item item = itemRepository.getById(id);
        User user = UserMapper.fromUserDto(userService.getUserById(userId));
        if (checkBeforeComment(userId, id)) {
            Comment comment = new Comment(commentRequestDto.getText(), item, user);

            return CommentMapper.mapToCommentDto(commentRepository.save(comment));
        } else {
            log.error("Ошиба валидации");
            throw new ValidException("Ошиба валидации");
        }
    }

    private boolean checkBeforeComment(int userId, int id) {
        dateTime = LocalDateTime.now();
        return bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(id, userId, BookingStatus.APPROVED, dateTime);

    }
}
