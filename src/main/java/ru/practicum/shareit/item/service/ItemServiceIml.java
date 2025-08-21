package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ProjectException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceIml implements ItemService{
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public Item addItem(long ownerId, Item item) {
        validate(ownerId);
        validate(item);

        return repository.addItem(ownerId, item);
    }

    @Override
    public Item updateItem(long itemId, long ownerId, Item item) {
        validate(ownerId);
        if (repository.getOwner(itemId) != ownerId) {
            throw new ProjectException("id пользователя "+ ownerId + " yне соответствует id владельца "+ repository.getOwner(itemId));
        }
        return repository.updateItem(item, itemId);
    }

    @Override
    public Item getItem(long itemId) {
        Item item = repository.getItem(itemId);
        if ( item == null )
            throw new ProjectException("itemId " + itemId + "не найден");
        return item;
    }

    @Override
    public List<Item> getUserItems(long ownerId) {
        validate(ownerId);
        return repository.getUserItems(ownerId);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return repository.searchItems(text);
    }

    private void validate(long ownerId) {
        if (!userRepository.ifUserExists(ownerId)) {
            throw new NotFoundException("пользователь с id "+ ownerId + " не найден");
        }
    }
    private void validate(Item item) {

    }

}
