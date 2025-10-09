package ru.practicum.shareit.request.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.repository.ItemEntityMapper;
import ru.practicum.shareit.item.repository.ItemRepository1;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestEntity;
import ru.practicum.shareit.request.repository.RequestEntityMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository1;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class RequestServiceImpl  implements RequestService{
    private final RequestRepository repository;
    private final RequestEntityMapper mapper;
    private final ItemRepository1 itemRepository;
    private final ItemEntityMapper itemMapper;
    private final UserRepository1 userRepository;

    @Override
    @Transactional
    public ItemRequest addRequest(ItemRequest itemRequest, long authorId) {
        RequestEntity requestEntity = mapper.toEntity(itemRequest);
        UserEntity author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("User not found: " + authorId));
        requestEntity.setAuthor(author);
        return  mapper.toItemRequest(repository.save(requestEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequest> getRequestsByAuthor(long authorId) {
        return repository.findByAuthor_IdOrderByCreatedDesc(authorId).stream()
                .map(mapper:: toItemRequest)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequest> getAllRequests() {
        return repository.findAll().stream()
                .map(mapper::toItemRequest)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequest getRequestById(long requestId) {
        return  mapper.toItemRequest(repository.findById(requestId));

    }


}
