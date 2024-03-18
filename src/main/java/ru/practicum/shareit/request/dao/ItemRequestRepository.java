package ru.practicum.shareit.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.modul.ItemRequest;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
}
