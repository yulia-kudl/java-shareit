package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserResponse;


/* это для ответа на запрос  GET  requests/    */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ItemReqDto {
    long id;
    String name;
   // UserResponse owner;

}
