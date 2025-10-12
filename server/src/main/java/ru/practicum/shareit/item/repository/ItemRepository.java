package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.entity.ItemEntity;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    @Query(" select i from ItemEntity i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))")
    List<ItemEntity> searchItems(String text);

    @Query("SELECT i FROM ItemEntity i WHERE i.owner.id = :ownerId")
    List<ItemEntity> findAllByOwner(@Param("ownerId") long ownerId);

    List<ItemEntity> findByRequest_Id(long requestId);
}