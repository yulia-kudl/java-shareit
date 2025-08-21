package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseUserDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService service;
    private final ItemMapper mapper;

    @PostMapping
    ItemResponseDto create( @Valid @RequestBody ItemRequestDto request, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return mapper.toItemResponse(service.addItem(ownerId, mapper.toItem(request)));
    }

    @PatchMapping("{itemId}")
    ItemResponseDto update(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @Valid @RequestBody ItemUpdateDto request,
                           @PathVariable long itemId) {
        return mapper.toItemResponse(service.updateItem(itemId, ownerId, mapper.toItem(request)));

    }

    @GetMapping("{itemId}")
    ItemResponseDto get(@PathVariable long itemId) {
        return mapper.toItemResponse(service.getItem(itemId));

    }

    @GetMapping
    List<ItemResponseUserDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return mapper.toItemResponseList(service.getUserItems(ownerId));
    }

    @GetMapping("search")
    List<ItemResponseUserDto> searchItems(@RequestParam  String text) {
        return mapper.toItemResponseList(service.searchItems(text));
    }
}
