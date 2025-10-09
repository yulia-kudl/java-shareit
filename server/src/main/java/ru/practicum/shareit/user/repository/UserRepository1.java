package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.entity.UserEntity;

public interface UserRepository1 extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
}
