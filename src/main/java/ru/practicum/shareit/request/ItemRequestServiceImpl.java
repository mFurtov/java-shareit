package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestsMapper;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import java.util.*;
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
        return addItems(repository.findByRequestorId(userId));
    }

    @Override
    public List<ItemRequestDto> getAllRequest(int userId,int from, Integer size){
        User user = UserMapper.fromUserDto(userService.getUserById(userId));
        List<ItemRequest> itemRequests = repository.getAllRequest(userId, Sort.by(Sort.Direction.DESC,"created"));
        size = (size != null) ? size : itemRequests.size();
        int index = Math.min(from+size,itemRequests.size());
        return addItems(itemRequests.subList(from,index));
    }

    private List<ItemRequestDto> addItems (List<ItemRequest> list){
        Map<Integer, ItemRequest> itemRequestMap = list.stream().collect(Collectors.toMap(ItemRequest::getId, itemRequest -> itemRequest));
        List<Integer> idRequestMap = itemRequestMap.values().stream().map(ItemRequest::getId).collect(Collectors.toList());
        Map<Integer, List<Item>> mapItems = itemRepository.findByRequestIdIn(idRequestMap).stream().collect(Collectors.groupingBy(item -> item.getRequest().getId()));
        List<ItemRequestDto> itemRequests = new ArrayList<>();
        for (Integer i : itemRequestMap.keySet()) {
            ItemRequestDto itemRequest = ItemRequestsMapper.mapFromItemRequest(itemRequestMap.get(i));
            if (mapItems.containsKey(i)) {
                itemRequest.setItems(ItemMapper.mapToListRequstDto(mapItems.get(i)));
            }
            itemRequests.add(itemRequest);
        }
        Comparator<ItemRequestDto> comparator = Comparator.comparing(ItemRequestDto::getCreated).reversed();
        Collections.sort(itemRequests, comparator);
        return itemRequests;
    }


}
