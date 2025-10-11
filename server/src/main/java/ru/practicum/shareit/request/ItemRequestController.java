package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final RequestService service;
    private final RequestMapper mapper;

    @PostMapping
    ReqResponseDto addRequest(@RequestBody RequestDto request, @RequestHeader("X-Sharer-User-Id") long authorId) {
        return mapper.toReqResponseDto(service.addRequest(mapper.toItemRequest(request), authorId));
    }

    @GetMapping
    List<ReqResponseFullDto> getAuthorRequests(@RequestHeader("X-Sharer-User-Id") long authorId ) {
        //проверка что такого пользователя нет?
        return mapper.toFullDtoList(service.getRequestsByAuthor(authorId));
    }

    @GetMapping("all")
    List<ReqResponseDto> getAllRequests() {
        return mapper.toReqResponseDtoList(service.getAllRequests());
    }

    @GetMapping("{requestId}")
    ReqResponseFullDto getRequestById(@PathVariable long requestId) {
        return mapper.toFullDto(service.getRequestById(requestId));
    }

}
