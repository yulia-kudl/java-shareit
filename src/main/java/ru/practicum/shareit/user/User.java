package ru.practicum.shareit.user;

import jakarta.persistence.*;
import lombok.*;

/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String name;
    private String email;
}
