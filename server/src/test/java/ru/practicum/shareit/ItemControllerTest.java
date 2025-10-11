package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService service;
    @MockBean
    private ItemMapper mapper;

    @Test
    void createItem_Ok() throws Exception {
        ItemRequestDto request = new ItemRequestDto("Item1", "Description1", true, null);
        ItemResponseDto response = new ItemResponseDto(1L, "Item1", "Description1", true, null, null, null, null, null);

        when(mapper.toItem(any(ItemRequestDto.class))).thenReturn(new Item());
        when(service.addItem(anyLong(), any(Item.class))).thenReturn(new Item());
        when(mapper.toItemResponse(any(Item.class))).thenReturn(response);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Item1"));
    }

    @Test
    void updateItem_Ok() throws Exception {
        ItemUpdateDto update = new ItemUpdateDto(1L, "Updated", "Updated Desc", true, null);
        ItemResponseDto response = new ItemResponseDto(1L, "Updated", "Updated Desc",
                true, null, null, null, null, null);

        when(mapper.toItem(any(ItemUpdateDto.class))).thenReturn(new Item());
        when(service.updateItem(anyLong(), anyLong(), any(Item.class))).thenReturn(new Item());
        when(mapper.toItemResponse(any(Item.class))).thenReturn(response);

        mockMvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void getItem_Ok() throws Exception {
        ItemResponseDto response = new ItemResponseDto(1L, "Item1", "Description1",
                true, null, null, null, null, null);

        when(service.getItem(anyLong(), anyLong())).thenReturn(new Item());
        when(mapper.toItemResponse(any(Item.class))).thenReturn(response);

        mockMvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserItems_Ok() throws Exception {
        ItemResponseUserDto response = new ItemResponseUserDto(1L, "Item1", "Desc", true, null);

        when(service.getUserItems(anyLong())).thenReturn(Collections.singletonList(new Item()));
        when(mapper.toItemResponseList(any())).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void searchItems_Ok() throws Exception {
        ItemResponseUserDto response = new ItemResponseUserDto(1L, "Item1", "Desc", true, null);

        when(service.searchItems(anyString())).thenReturn(Collections.singletonList(new Item()));
        when(mapper.toItemResponseList(any())).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/items/search")
                        .param("text", "Item1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Item1"));
    }

    @Test
    void addComment_Ok() throws Exception {
        CommentDto comment = new CommentDto(1L, "Nice item", "User", null);
        CommentDto response = new CommentDto(1L, "Nice item", "User", null);

        when(mapper.toComment(any(CommentDto.class))).thenReturn(new Comment());
        when(service.addComment(anyLong(), anyLong(), any(Comment.class))).thenReturn(new Comment());
        when(mapper.toCommentDto(any(Comment.class))).thenReturn(response);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Nice item"));
    }
}
