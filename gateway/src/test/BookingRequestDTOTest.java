import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingRequestDTOTest {

    @Autowired
    private JacksonTester<BookingRequestDto> json;

    @Test
    void testBookingRequestDtoSerialization() throws Exception {
        LocalDateTime start = LocalDateTime.of(2025, 10, 12, 14, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 10, 13, 14, 0, 0);

        BookingRequestDto dto = new BookingRequestDto(1L, start, end);

        JsonContent<BookingRequestDto> result = json.write(dto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-10-12T14:00:00");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-10-13T14:00:00");
    }

    @Test
    void testStartBeforeEndValidation() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 14, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 10, 13, 12, 0);

        BookingRequestDto dto = new BookingRequestDto(1L, start, end);

        assertThat(dto.isStartBeforeEnd()).isFalse();
    }

    @Test
    void testStartBeforeEndValidationTrue() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 12, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 10, 13, 12, 0);

        BookingRequestDto dto = new BookingRequestDto(1L, start, end);

        assertThat(dto.isStartBeforeEnd()).isTrue();
    }
}