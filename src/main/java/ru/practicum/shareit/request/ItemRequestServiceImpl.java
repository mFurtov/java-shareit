package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestsMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRequestRepository repository;
    public final UserService userService;
    @Override
    public ItemRequestDto postRequest(int userId, ItemRequestCreateDto itemRequestCreateDto) {
        User user = UserMapper.fromUserDto(userService.getUserById(userId)) ;
        return ItemRequestsMapper.mapFromItemRequest(repository.save(ItemRequestsMapper.mapToItemRequest(itemRequestCreateDto,user)));
    }
}
