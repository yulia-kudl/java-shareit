package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/* это для ответа на запрос  GET  requests/    */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ItemReqDto {
    long id;
    String name;
   // UserResponse owner;

}
