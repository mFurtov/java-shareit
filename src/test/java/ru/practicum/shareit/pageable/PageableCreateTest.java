package ru.practicum.shareit.pageable;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

class PageableCreateTest {

    @Test
    void getPageable() {
        int from = 20;
        int size = 10;
        Sort sort = Sort.by("fieldName");

        Pageable pageable = PageableCreate.getPageable(from, size, sort);

        assertEquals(PageRequest.of(2, 10, sort), pageable);

    }
}