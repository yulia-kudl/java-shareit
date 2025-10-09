package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(long ownerId, ItemRequestDto request) {
        return post("", ownerId, request);
    }

    public ResponseEntity<Object> updateItem(long itemId, long ownerId,  ItemUpdateDto request) {
        return patch("/" + itemId, ownerId, request);
    }

    public ResponseEntity<Object> getItem(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getUserItems(long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> searchItems(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("search?text={text}", null, parameters);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId,  CommentDto comment) {
        return post("/" + itemId +"/comment", userId, comment);
    }
}
