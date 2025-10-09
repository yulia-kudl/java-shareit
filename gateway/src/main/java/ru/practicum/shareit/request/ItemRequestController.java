package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ReqResponseDto;
import ru.practicum.shareit.request.dto.ReqResponseFullDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final RequestClient requestClient;

    @PostMapping
    ResponseEntity<Object> addRequest(@Valid @RequestBody RequestDto request, @RequestHeader("X-Sharer-User-Id") long authorId) {
        return requestClient.addRequest(authorId, request);
    }

    @GetMapping
    ResponseEntity<Object> getAuthorRequests(@RequestHeader("X-Sharer-User-Id") long authorId ) {
        //проверка что такого пользователя нет?
        return requestClient.getRequestsByAuthor(authorId);
    }

    @GetMapping("all")
    ResponseEntity<Object> getAllRequests() {
        return requestClient.getAllRequests();
    }

    @GetMapping("{requestId}")
    ResponseEntity<Object> getRequestById(@PathVariable long requestId) {
        return requestClient.getRequestById(requestId);
    }

}
