package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserResponse;

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
    ItemResponseDto create(@Valid @RequestBody ItemRequestDto request, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return mapper.toItemResponse(service.addItem(ownerId, mapper.toItem(request)));
    }

    @PatchMapping("{itemId}")
    ItemResponseDto update(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @Valid @RequestBody ItemUpdateDto request,
                           @PathVariable long itemId) {
        return mapper.toItemResponse(service.updateItem(itemId, ownerId, mapper.toItem(request)));

    }

    @GetMapping("{itemId}")
    ItemResponseDto get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return mapper.toItemResponse(service.getItem(itemId, userId));

    }

    @GetMapping
    List<ItemResponseUserDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return mapper.toItemResponseList(service.getUserItems(ownerId));
    }

    @GetMapping("search")
    List<ItemResponseUserDto> searchItems(@RequestParam String text) {
        return mapper.toItemResponseList(service.searchItems(text));
    }

    //POST /items/{itemId}/comment
    @PostMapping("/{itemId}/comment")
    CommentDto addComment(@Valid @RequestBody CommentDto comment, @PathVariable long itemId,
                            @RequestHeader("X-Sharer-User-Id") long userId ) {
        return mapper.toCommentDto(service.addComment(userId, itemId, mapper.toComment(comment)));
    }
}
