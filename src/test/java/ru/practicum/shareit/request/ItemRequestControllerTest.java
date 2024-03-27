package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@DirtiesContext
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void postRequest() throws Exception {
        ItemRequestCreateDto requestCreateDto = new ItemRequestCreateDto();
        requestCreateDto.setDescription("Test");

        ItemRequestDto requestDto = new ItemRequestDto(1, "Test", LocalDateTime.now());
        when(itemRequestService.postRequest(anyInt(), any())).thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDto.getId()));

        requestCreateDto.setDescription("");
        mvc.perform(post("/requests")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCreateDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "Test", LocalDateTime.now());
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);

        when(itemRequestService.getRequest(anyInt())).thenReturn(itemRequestDtoList);

        mvc.perform(get("/requests")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(itemRequestDtoList.size())));

    }

    @Test
    void getAllRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "Test", LocalDateTime.now().plusMinutes(1));
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);

        when(itemRequestService.getAllRequestPage(anyInt(), anyInt(), anyInt())).thenReturn(itemRequestDtoList);

        mvc.perform(get("/requests/all")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(itemRequestDtoList.size())));

        mvc.perform(get("/requests/all")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", "-1"))
                .andExpect(status().isBadRequest());


        mvc.perform(get("/requests/all")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "0"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getRequestById() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "Test", LocalDateTime.now().plusMinutes(1));
        when(itemRequestService.getRequestById(anyInt(), anyInt())).thenReturn(itemRequestDto);

        mvc.perform(get("/requests/{id}", 1)
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()));
    }
}