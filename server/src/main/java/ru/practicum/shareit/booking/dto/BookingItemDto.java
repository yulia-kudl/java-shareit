package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.dto.UserResponse;

import java.time.LocalDateTime;

/* это DTO используется для ответа в ItemResponseDTO для last и next booking */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingItemDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private UserResponse booker;

}
