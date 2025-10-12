package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.entity.RequestEntity;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestEntityMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemRequestServiceUnitTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestEntityMapper mapper;

    @InjectMocks
    private RequestServiceImpl service;

    private UserEntity author;
    private ItemRequest request;
    private RequestEntity requestEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        author = new UserEntity(1L, "Author", "author@mail.com");
        request = new ItemRequest(
                null,
                LocalDateTime.now(),
                author.getId(),
                "Ищу дрель",
                null
        );
        requestEntity = new RequestEntity(
                1L,
                "Ищу дрель",
                author,
                LocalDateTime.now(),
                null
        );
    }

    @Test
    void addRequest_savesRequest_whenUserExists() {
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(mapper.toEntity(any())).thenReturn(requestEntity);
        when(requestRepository.save(any())).thenReturn(requestEntity);
        when(mapper.toItemRequest(requestEntity)).thenReturn(request);

        ItemRequest result = service.addRequest(request, author.getId());

        assertNotNull(result);
        assertEquals("Ищу дрель", result.getDescription());

        // проверка вызовов
        verify(userRepository).findById(author.getId());
        verify(requestRepository).save(any(RequestEntity.class));
    }

    @Test
    void addRequest_throwsException_whenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.addRequest(request, 999L));

        verify(userRepository).findById(999L);
        verify(requestRepository, never()).save(any());
    }

    @Test
    void getRequestsByAuthor_returnsDescendingList() {
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(requestRepository.findByAuthor_IdOrderByCreatedDesc(author.getId()))
                .thenReturn(List.of(requestEntity));
        when(mapper.toItemRequest(requestEntity)).thenReturn(request);

        List<ItemRequest> result = service.getRequestsByAuthor(author.getId());

        assertEquals(1, result.size());
        assertEquals("Ищу дрель", result.get(0).getDescription());

        verify(userRepository, times(1)).findById(author.getId());
        verify(requestRepository).findByAuthor_IdOrderByCreatedDesc(author.getId());
    }
}
