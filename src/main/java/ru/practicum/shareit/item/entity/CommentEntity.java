package ru.practicum.shareit.item.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.entity.UserEntity;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;
    @CreationTimestamp
    @Column(name = "registry", updatable = false)
    private Timestamp registry;
}
