package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.RequestEntity;

import java.util.List;


public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    @EntityGraph(attributePaths = "items")
    RequestEntity findById(long id);

    @EntityGraph(attributePaths = "items")
    List<RequestEntity> findByAuthor_IdOrderByCreatedDesc(long authorId);
}
