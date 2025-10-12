package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ProjectException;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemEntityMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ItemServiceIml implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final ItemEntityMapper mapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final BookingService bookingService;

    @Override
    public Item addItem(long ownerId, Item item) {
        validate(ownerId);
        validate(item);
        ItemEntity itemEntity = mapper.toEntity(item, userRepository);
        UserEntity userEntity = userRepository.findById(ownerId).orElseThrow(() -> new NoSuchElementException("owner id" + ownerId));
        itemEntity.setOwner(userEntity);

        return mapper.toItem(repository.save(itemEntity));
    }

    @Override
    public Item updateItem(long itemId, long ownerId, Item item) {
        validate(ownerId);
        ItemEntity itemToUpdate = repository.findById(itemId).orElseThrow(() -> new NoSuchElementException("item c ID "
                + itemId + "не существует"));
        Long currentOwner = itemToUpdate.getOwner().getId();
        if (!currentOwner.equals(ownerId)) {
            throw new ProjectException("id пользователя " + ownerId + " не соответствует id владельца " +
                    currentOwner);
        }
        mapper.updateEntity(item, itemToUpdate);
        return mapper.toItem(repository.save(itemToUpdate));
    }

    @Override
    public Item getItem(long itemId, long userId) {
        ItemEntity itemEntity = repository.findById(itemId).orElseThrow(() -> new ProjectException("itemId " + itemId + " не найден"));
        List<Comment> comments = mapper.toCommentList(commentRepository.findAllByItem_Id(itemId));
        Item item = mapper.toItem(itemEntity);
        item.setComments(comments);
        if (itemEntity.getOwner().getId() == userId) {
            item.setNextBooking(bookingService.getNextBookingForItem(itemId));
            item.setLastBooking(bookingService.getLastBookingForItem(itemId));
        }
        return item;
    }

    @Override
    public List<Item> getUserItems(long ownerId) {
        validate(ownerId);
        return repository.findAllByOwner(ownerId).stream()
                .map(mapper::toItem)
                .toList();
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return repository.searchItems(text).stream()
                .filter(ItemEntity::getAvailable)
                .map(mapper::toItem)
                .toList();
    }

    @Override
    public Comment addComment(long userId, long itemId, Comment comment) {
        Timestamp timestamp = Timestamp.from(Instant.now());
        if (bookingRepository.findFirstByItem_IdAndUser_IdAndEndBefore(itemId, userId, timestamp).isEmpty())
            throw new ProjectException("завершенной брони вещи с itemId " + itemId + " у пользователя userId " + userId
                    + " нет на момент" + timestamp.toLocalDateTime());


        UserEntity userEntity = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        ItemEntity itemEntity = repository.findById(itemId).orElseThrow(NoSuchElementException::new);


        CommentEntity commentEntity = mapper.toEntity(comment);
        commentEntity.setItem(itemEntity);
        commentEntity.setAuthor(userEntity);
        return mapper.toComment(commentRepository.save(commentEntity));
    }

    private void validate(long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("пользователь с id " + ownerId + " не найден");
        }
    }

    private void validate(Item item) {

    }

}
