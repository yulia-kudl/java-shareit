package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingEntityMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.BookingState;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ProjectException;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository1;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository1;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository repository;
    private final BookingEntityMapper mapper;
    private final UserRepository1 userRepository;
    private final ItemRepository1 itemRepository;

    @Override
    public Booking addBooking(long bookerId, Booking booking) {
        UserEntity userEntity= userRepository.findById(bookerId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь с id=" + bookerId + " не найден"));
        ItemEntity itemEntity= itemRepository.findById(booking.getItemId())
                .orElseThrow(() ->
                        new NotFoundException("Item с id=" + booking.getItemId() + " не найден"));
        if (!(itemEntity.getAvailable())) {
            throw new ProjectException("unavailable item");
        }
        BookingEntity bookingEntity = mapper.toEntity(booking);
        bookingEntity.setUser(userEntity);
        bookingEntity.setItem(itemEntity);
        bookingEntity.setStatus(BookingStatus.WAITING.name());

        return mapper.toBooking(repository.save(bookingEntity));
    }

    @Override
    public Booking updateBookingStatus(long bookingId, long ownerId, boolean approved) {
        checkOwnerId(bookingId, ownerId);
        BookingEntity bookingToUpdate = repository.findById(bookingId).orElseThrow();
        bookingToUpdate.setStatus(String.valueOf(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED));
        return mapper.toBooking(repository.save(bookingToUpdate));
    }


    @Override
    public Booking getBooking(long requester, long bookingId) {
        checkOwnerOrBookerId(requester, bookingId);

        return mapper.toBooking(repository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException("Бронирование с id=" + bookingId + " не найдено")));
    }

    @Override
    public List<Booking> getBookingsForBookerByState(long booker, BookingState state) {
        checkIfUserExists(booker);
        switch (state) {
            case BookingState.ALL -> {
                return repository.findAllByBookerAndState(booker).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
            case BookingState.CURRENT -> {
                return repository.findCurrentByBookerAndState(booker, Timestamp.valueOf(LocalDateTime.now())).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
            case BookingState.FUTURE ->  {
                return repository.findFutureByBookerAndState(booker, Timestamp.valueOf(LocalDateTime.now())).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
            case BookingState.PAST -> {
                return repository.findPastByBookerAndState(booker, Timestamp.valueOf(LocalDateTime.now())).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
            case BookingState.REJECTED, BookingState.WAITING -> {
                return repository.findByBookerAndState(booker, state.name()).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
        }
       return List.of();
    }

    @Override
    public List<Booking> getBookingsForOwnerByState(long owner, BookingState state) {
        checkIfUserExists(owner);
        switch (state) {
            case BookingState.ALL -> {
                return repository.findAllByOwnerAndState(owner,state.name()).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
            case BookingState.CURRENT -> {
                return repository.findCurrentByOwnerAndState(owner, state.name()).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
            case BookingState.FUTURE ->  {
                return repository.findFutureByOwnerAndState(owner, state.name()).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
            case BookingState.PAST -> {
                return repository.findPastByOwnerAndState(owner, state.name()).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
            case BookingState.REJECTED, BookingState.WAITING -> {
                return repository.findByOwnerAndState(owner, state.name()).stream()
                        .map(mapper::toBooking)
                        .toList();
            }
        }
        return List.of();

    }

    @Override
    public Booking getNextBookingForItem(long itemId) {
        return mapper.toBooking(repository.findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId,
                Timestamp.valueOf(LocalDateTime.now())));
    }

    @Override
    public Booking getLastBookingForItem(long itemId) {
        return mapper.toBooking(repository.findFirstByItem_IdAndEndBeforeOrderByStartDesc(itemId, Timestamp.valueOf(LocalDateTime.now())));
    }

    private void checkIfUserExists(long user) {
        userRepository.findById(user).orElseThrow(() ->
                new EntityNotFoundException("Пользователь с id=" + user + " не найден"));

    }

    private void checkOwnerId(long bookingId, long ownerId) {
        repository.findFirstByIdAndItemOwnerId(bookingId, ownerId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь с id=" + ownerId + " не найден"));
    }
    private void checkOwnerOrBookerId(long requester, long bookingId) {
        if (/*repository.findFirstByIdAndOwner(bookingId, requester).isEmpty() ||*/
                repository.findFirstByIdAndItemOwnerId(bookingId,requester).isEmpty() &&
        (repository.findFirstByIdAndUserId(bookingId,requester).isEmpty()))
            throw new NotFoundException("не найдено");
    }
}