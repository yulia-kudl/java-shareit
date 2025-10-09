package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto request, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemClient.addItem(ownerId, request);
    }

    @PatchMapping("{itemId}")
    ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @Valid @RequestBody ItemUpdateDto request,
                           @PathVariable long itemId) {
        return itemClient.updateItem(itemId, ownerId, request);

    }

    @GetMapping("{itemId}")
    ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return itemClient.getItem(itemId, userId);

    }

    @GetMapping
    ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemClient.getUserItems(ownerId);
    }

    @GetMapping("search")
    ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemClient.searchItems(text);
    }

    //POST /items/{itemId}/comment
    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto comment, @PathVariable long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.addComment(userId, itemId, comment);
    }
}
