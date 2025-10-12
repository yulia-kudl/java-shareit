package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.repository.ItemEntityMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.RequestEntity;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestEntityMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final RequestEntityMapper mapper;
    private final ItemRepository itemRepository;
    private final ItemEntityMapper itemMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequest addRequest(ItemRequest itemRequest, long authorId) {
        RequestEntity requestEntity = mapper.toEntity(itemRequest);
        UserEntity author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("User not found: " + authorId));
        requestEntity.setAuthor(author);
        return mapper.toItemRequest(repository.save(requestEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequest> getRequestsByAuthor(long authorId) {
        UserEntity author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("User not found: " + authorId));
        return repository.findByAuthor_IdOrderByCreatedDesc(author.getId()).stream()
                .map(mapper::toItemRequest)
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
        return mapper.toItemRequest(repository.findById(requestId));

    }


}
