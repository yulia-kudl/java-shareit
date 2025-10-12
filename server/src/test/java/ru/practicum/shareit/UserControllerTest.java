package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.Service.UserService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.mapper.UserMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserMapper mapper;
    @MockBean
    private UserService service;

    @Test
    void createUser_Ok() throws Exception {
        UserRequest request = new UserRequest("User", "user@mail.com");
        UserResponse response = new UserResponse(1L, "User", "user@mail.com");

        when(mapper.toUser(any(UserRequest.class))).thenReturn(new User());
        when(service.createUser(any(User.class))).thenReturn(new User());
        when(mapper.toUserResponse(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User"))
                .andExpect(jsonPath("$.email").value("user@mail.com"));
    }

    @Test
    void getUserById_Ok() throws Exception {
        long userId = 1L;
        UserResponse response = new UserResponse(userId, "User", "user@mail.com");

        when(service.getUser(userId)).thenReturn(new User());
        when(mapper.toUserResponse(any(User.class))).thenReturn(response);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("User"));
    }

    @Test
    void updateUser_Ok() throws Exception {
        long userId = 1L;
        UserUpdate update = new UserUpdate("New Name", "new@mail.com");
        UserResponse response = new UserResponse(userId, "New Name", "new@mail.com");

        when(mapper.toUser(any(UserUpdate.class))).thenReturn(new User());
        when(service.updateUser(any(User.class), eq(userId))).thenReturn(new User());
        when(mapper.toUserResponse(any(User.class))).thenReturn(response);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.email").value("new@mail.com"));
    }

    @Test
    void deleteUser_Ok() throws Exception {
        long userId = 1L;

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
    }
}
