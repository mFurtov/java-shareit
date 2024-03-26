package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@DirtiesContext
class BookingControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;


    @Test
    void postBooking() throws Exception {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest(1, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(1));
        BookingDto bookingDto = new BookingDto(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), null, null, BookingStatus.WAITING);

        when(bookingService.postBooking(anyInt(), any())).thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));

        bookingDtoRequest.setStart(LocalDateTime.now().minusHours(1));
        mvc.perform(post("/bookings")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest)))
                .andExpect(status().isBadRequest());

        bookingDtoRequest.setEnd(LocalDateTime.now());
        mvc.perform(post("/bookings")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void patchBookingApproved() throws Exception {
        BookingDto bookingDto = new BookingDto(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), null, null, BookingStatus.WAITING);
        when(bookingService.patchBookingApproved(anyInt(), anyInt(), eq(true))).thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    void getBooking() throws Exception {
        BookingDto bookingDto = new BookingDto(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), null, null, BookingStatus.WAITING);

        when(bookingService.getBooking(anyInt(), anyInt())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    void getBookingWithStatus() throws Exception {
        BookingDto bookingDto = new BookingDto(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), null, null, BookingStatus.WAITING);
        List<BookingDto> bookingDtoList = List.of(bookingDto);

        when(bookingService.getBookingWithStatus(anyInt(), any(), any())).thenReturn(bookingDtoList);

        mvc.perform(get("/bookings")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(bookingDtoList.size()));

        mvc.perform(get("/bookings")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .param("state", "ALL")
                        .param("from", "-1"))
                .andExpect(status().isInternalServerError());

        mvc.perform(get("/bookings")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .param("state", "ALL")
                        .param("size", "0"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getBookingWithStatusOwner() throws Exception {
        BookingDto bookingDto = new BookingDto(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), null, null, BookingStatus.WAITING);
        List<BookingDto> bookingDtoList = List.of(bookingDto);

        when(bookingService.getBookingWithStatusOwner(anyInt(), any(), any())).thenReturn(bookingDtoList);

        mvc.perform(get("/bookings/owner")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(bookingDtoList.size()));

        mvc.perform(get("/bookings")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .param("state", "ALL")
                        .param("from", "-1"))
                .andExpect(status().isInternalServerError());

        mvc.perform(get("/bookings")
                        .header(HeaderConstants.X_SHARER_USER_ID, 123)
                        .param("state", "ALL")
                        .param("size", "0"))
                .andExpect(status().isInternalServerError());

    }
}