package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.entity.BookingEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {


    @Query("select b from BookingEntity b where b.user.id = :userId  order by start desc")
    List<BookingEntity> findAllByBookerAndState(@Param("userId") long bookerId);

    @Query("select b from BookingEntity b where b.user.id = :userId and start > :now and end < :now order by start desc")
    List<BookingEntity> findCurrentByBookerAndState(@Param("userId") long booker, @Param("now") Timestamp now);

    @Query("select b from BookingEntity b where b.user.id = :userId and start > :now and end < :now order by start desc")
    List<BookingEntity> findFutureByBookerAndState(@Param("userId") long booker, @Param("now") Timestamp now);

    @Query("select b from BookingEntity b where b.user.id = :userId and end < :now order by start desc")
    List<BookingEntity> findPastByBookerAndState(@Param("userId") long booker, @Param("now") Timestamp now);

    @Query("select b from BookingEntity b where b.user.id = :userId and b.status = :state order by start desc")
    List<BookingEntity> findByBookerAndState(@Param("userId") long booker, @Param(("state")) String state);

    @Query("select b from BookingEntity b")
    List<BookingEntity> findAllByOwnerAndState(@Param("userId") long owner, String name);

    @Query("select b from BookingEntity b")
    List<BookingEntity> findCurrentByOwnerAndState(@Param("userId") long owner, String name);

    @Query("select b from BookingEntity b")
    List<BookingEntity> findFutureByOwnerAndState(@Param("userId") long owner, String name);

    @Query("select b from BookingEntity b")
    List<BookingEntity> findPastByOwnerAndState(@Param("userId") long owner, String name);

    @Query("select b from BookingEntity b")
    List<BookingEntity> findByOwnerAndState(@Param("userId") long owner, String name);


    // Optional<BookingEntity> findFirstByIdAndOwner(long id, long owner);

    Optional<BookingEntity> findFirstByIdAndItemOwnerId(long bookingId, long requester);

    Optional<BookingEntity> findFirstByIdAndUserId(long bookingId, long requester);

    Optional<BookingEntity> findFirstByItem_IdAndUser_IdAndEndBefore(long itemId, long userId, Timestamp timestamp);

    BookingEntity findFirstByItem_IdAndStartAfterOrderByStartAsc(long itemId, Timestamp timestamp);

    BookingEntity findFirstByItem_IdAndEndBeforeOrderByStartDesc(long itemId, Timestamp timestamp);
}
