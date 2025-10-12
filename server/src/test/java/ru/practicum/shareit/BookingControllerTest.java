package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private BookingMapper mapper;

    @Test
    void createBooking_Ok() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto(1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1));

        Booking booking = new Booking();
        booking.setId(1L);

        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(1L);

        when(mapper.toBooking((BookingResponseDto) any())).thenReturn(booking);
        when(bookingService.addBooking(anyLong(), any())).thenReturn(booking);
        when(mapper.toBookingDto(any())).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateBooking_Ok() throws Exception {
        Booking booking = new Booking();
        booking.setId(10L);

        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(10L);

        when(bookingService.updateBookingStatus(eq(10L), eq(1L), eq(true))).thenReturn(booking);
        when(mapper.toBookingDto(booking)).thenReturn(responseDto);

        mockMvc.perform(patch("/bookings/10")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void getBooking_Ok() throws Exception {
        Booking booking = new Booking();
        booking.setId(5L);

        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(5L);

        when(bookingService.getBooking(eq(1L), eq(5L))).thenReturn(booking);
        when(mapper.toBookingDto(booking)).thenReturn(responseDto);

        mockMvc.perform(get("/bookings/5")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void getBookingsForBookerByState_Ok() throws Exception {
        Booking booking = new Booking();
        booking.setId(100L);

        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(100L);

        when(bookingService.getBookingsForBookerByState(eq(1L), eq(BookingState.ALL)))
                .thenReturn(List.of(booking));
        when(mapper.toBookingDto(booking)).thenReturn(dto);

        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100));
    }

    @Test
    void getBookingsForOwnerByState_Ok() throws Exception {
        Booking booking = new Booking();
        booking.setId(200L);

        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(200L);

        when(bookingService.getBookingsForOwnerByState(eq(1L), eq(BookingState.ALL)))
                .thenReturn(List.of(booking));
        when(mapper.toBookingDto(booking)).thenReturn(dto);

        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(200));
    }
}
