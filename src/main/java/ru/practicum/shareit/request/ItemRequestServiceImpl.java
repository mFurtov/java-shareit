package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestsMapper;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    public final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto postRequest(int userId, ItemRequestCreateDto itemRequestCreateDto) {
        User user = UserMapper.fromUserDto(userService.getUserById(userId));
        return ItemRequestsMapper.mapFromItemRequest(repository.save(ItemRequestsMapper.mapToItemRequest(itemRequestCreateDto, user)));
    }

    @Override
    public ItemRequest getAllRequest(int id) {
        Optional<ItemRequest> optionalItemRequest = repository.findById(id);
        return optionalItemRequest.orElse(null);
    }

    @Override
    public List<ItemRequestDto> getRequest(int userId) {
        User user = UserMapper.fromUserDto(userService.getUserById(userId));

        Map<Integer, ItemRequest> itemRequestMap = repository.findByRequestorId(userId).stream().collect(Collectors.toMap(ItemRequest::getId, itemRequest -> itemRequest));
        Map<Integer, Item> mapItems = itemRepository.findByRequestId(userId).stream().collect(Collectors.toMap(item -> item.getRequest().getId(), item -> item));
        List<ItemRequestDto> itemRequests = new ArrayList<>();
        for(Integer i: itemRequestMap.keySet()){
            if (mapItems.containsKey(i)) {
                ItemRequestDto itemRequest = ItemRequestsMapper.mapFromItemRequest(itemRequestMap.get(i));
                Item item = mapItems.get(i);
                itemRequest.setItem(item);
                itemRequests.add(itemRequest);
            }
        }


        return itemRequests;
    }


}
