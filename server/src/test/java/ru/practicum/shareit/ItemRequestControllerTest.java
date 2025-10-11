package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapperJson;

    @MockBean
    private RequestService service;

    @MockBean
    private RequestMapper mapper;

    @Test
    void addRequest_Ok() throws Exception {
        RequestDto requestDto = new RequestDto("Нужна комната в Москве");
        ReqResponseDto responseDto = new ReqResponseDto(1L, LocalDateTime.now(), "Нужна комната в Москве");

        when(service.addRequest(any(), anyLong())).thenReturn(new ItemRequest());
        when(mapper.toItemRequest(any(RequestDto.class))).thenReturn(new ItemRequest());
        when(mapper.toReqResponseDto(any(ItemRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapperJson.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна комната в Москве"));
    }

    @Test
    void getAuthorRequests_Ok() throws Exception {
        ReqResponseFullDto dto = new ReqResponseFullDto(1L, LocalDateTime.now(), List.of(), "Нужна комната в Москве");
        when(service.getRequestsByAuthor(anyLong())).thenReturn(List.of(new ItemRequest()));
        when(mapper.toFullDtoList(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Нужна комната в Москве"));
    }

    @Test
    void getAllRequests_Ok() throws Exception {
        ReqResponseDto dto = new ReqResponseDto(2L, LocalDateTime.now(), "Нужна комната в Москве");
        when(service.getAllRequests()).thenReturn(List.of(new ItemRequest()));
        when(mapper.toReqResponseDtoList(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].description").value("Нужна комната в Москве"));
    }

    @Test
    void getRequestById_Ok() throws Exception {
        ReqResponseFullDto dto = new ReqResponseFullDto(1L, LocalDateTime.now(), List.of(), "Нужна комната в Москве");
        when(service.getRequestById(anyLong())).thenReturn(new ItemRequest());
        when(mapper.toFullDto(any())).thenReturn(dto);

        mockMvc.perform(get("/requests/{requestId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна комната в Москве"));
    }
}
