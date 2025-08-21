package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long lastIndex = 0L;

    @Override
    public Item addItem(long ownerId, Item item) {
        setId(item);
        item.setOwner(ownerId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public long getOwner(long itemId) {
        return items.get(itemId).getOwner();
    }

    @Override
    public Item updateItem(Item item, long itemId) {
        Item currentItem = items.get(itemId);
        if (item.getName() != null) {
            currentItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            currentItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            currentItem.setAvailable(item.getAvailable());
        }

        return currentItem;
    }

    @Override
    public Item getItem(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getUserItems(long ownerId) {
        return items.values().stream()
        .filter(item -> item.getOwner() == ownerId)
                .toList();
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values().stream()
        .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
        .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .toList();
    }

    private void setId(Item item) {
        lastIndex++;
        item.setId(lastIndex);
    }
}
