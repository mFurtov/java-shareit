package ru.practicum.shareit.pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableCreate {
    public static Pageable getPageable(int from, int size, Sort sort) {
        return PageRequest.of(from / size, size, sort);
    }
}
