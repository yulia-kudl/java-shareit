package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface RequestService {
    ItemRequest addRequest(ItemRequest itemRequest, long authorId);

    List<ItemRequest> getRequestsByAuthor(long authorId);

    List<ItemRequest> getAllRequests();

    ItemRequest getRequestById(long requestId);
}
