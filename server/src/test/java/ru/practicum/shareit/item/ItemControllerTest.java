package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@DirtiesContext
class ItemControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;


    @Test
    void getItem() throws Exception {
        ItemDto itemDto = new ItemDto(1, "test", "test", true, 2);
        List<ItemDto> itemDtoList = List.of(itemDto);
        when(itemService.getItems(anyInt(), any(Pageable.class))).thenReturn(itemDtoList);

        mvc.perform(get("/items")
                        .header(HeaderConstants.X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(itemDtoList.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(itemDtoList.get(0).getName())));

    }

    @Test
    void getItemId() throws Exception {
        ItemDto itemDto = new ItemDto(1, "test", "test", true, 2);
        when(itemService.getItemId(anyInt(), anyInt())).thenReturn(itemDto);
        mvc.perform(get("/items/{id}", 1)
                        .header(HeaderConstants.X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())));
    }

    @Test
    void searchItems() throws Exception {
        ItemDto itemDto = new ItemDto(1, "test", "test", true, 2);
        List<ItemDto> itemDtoList = List.of(itemDto);
        String string = "Test";
        when(itemService.searchItems(eq(string), any(Pageable.class))).thenReturn(itemDtoList);
        mvc.perform(get("/items/search")
                        .param("text", string)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(itemDtoList.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(itemDtoList.get(0).getName())));
    }

    @Test
    void postItem() throws Exception {
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Test Item");
        itemCreateDto.setDescription("Test Description");
        itemCreateDto.setAvailable(true);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        when(itemService.postItem(eq(123), eq(itemCreateDto))).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.available").value(true));

    }

    @Test
    void patchItem() throws Exception {
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Updated Item");
        itemCreateDto.setDescription("Updated Description");
        itemCreateDto.setAvailable(false);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Updated Item");
        itemDto.setDescription("Updated Description");
        itemDto.setAvailable(false);

        when(itemService.patchItem(anyInt(), anyInt(), eq(itemCreateDto))).thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Item"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.available").value(false));

    }

    @Test
    void postComments() throws Exception {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Test Comment");

        CommentDto commentDto = new CommentDto(1, "Test Comment", "Test", LocalDateTime.now());

        when(itemService.postComments(anyInt(), anyInt(), eq(commentRequestDto))).thenReturn(commentDto);

        mvc.perform(post("/items/123/comment")
                        .header(HeaderConstants.X_SHARER_USER_ID, 456)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Test Comment"))
                .andExpect(jsonPath("$.authorName").value("Test"));

    }

}