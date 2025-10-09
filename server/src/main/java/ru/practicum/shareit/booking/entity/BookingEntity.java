package ru.practicum.shareit.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private Timestamp start;
    @Column(name = "end_date")
    private Timestamp end;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private UserEntity user;
    private String status;

}

