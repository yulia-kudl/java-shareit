package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemReqDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    private LocalDateTime created;
    private Long authorId;
    private String description;
    private List<ItemReqDto> items;

}
