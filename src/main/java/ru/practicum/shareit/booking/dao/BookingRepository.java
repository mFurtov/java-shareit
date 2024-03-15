package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    default void findByIdOrThrow(Integer id) {
        Optional<Booking> booking = findById(id);
        if (booking.isEmpty()) {
            throw new EntityNotFoundException("Объект не найден");
        }
    }

    List<Booking> findByItemIn(List<Item> itemList);

    @Query("select b from Booking b join b.item i where b.id = ?2 and (b.booker.id = ?1 or i.owner.id = ?1)")
    Booking searchBooking(int requestId, int bookingId);

    default Booking searchBookingOrThrow(int requestId, int bookingId) {
        Booking booking = searchBooking(requestId, bookingId);
        if (booking == null) {
            throw new EntityNotFoundException("Обьект не найден");
        }
        return booking;
    }

    @Query("SELECT b FROM Booking b WHERE b.start <= ?1 and b.end >= ?1 and b.booker.id = ?2")
    List<Booking> findCurrentBookings(LocalDateTime data, int id, Sort sort);

    List<Booking> findByBookerIdOrderByStartDesc(int id);

    @Query("SELECT b FROM Booking b WHERE b.end  <= ?1 and b.booker.id = ?2 and b.status IN ('WAITING', 'APPROVED')")
    List<Booking> findPastBookings(LocalDateTime data, int id, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.start >= ?1 and b.booker.id = ?2 and b.status IN ('WAITING', 'APPROVED')")
    List<Booking> findFutureBookings(LocalDateTime data, int id, Sort sort);

    List<Booking> findByStatusAndBookerIdOrderByStartDesc(BookingStatus status, int id);

    List<Booking> findByItemIdAndItemOwnerIdAndStatusNotOrderByStart(int itemId, int userId, BookingStatus bookingStatus);

    boolean existsByItemIdAndBookerIdAndStatusAndEndBefore(int itemId, int bookerId, BookingStatus status, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking b WHERE b.start <= ?1 AND b.end >= ?1 and b.item.owner.id = ?2")
    List<Booking> findCurrentBookingsOwner(LocalDateTime data, int id, Sort sort);

    List<Booking> findByItemOwnerIdOrderByStartDesc(int id);

    @Query("SELECT b FROM Booking b WHERE b.end <= ?1 and b.item.owner.id = ?2 and b.status IN ('WAITING', 'APPROVED')")
    List<Booking> findPastBookingsOwner(LocalDateTime data, int id, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.start >= ?1 and b.item.owner.id = ?2 and b.status IN ('WAITING', 'APPROVED')")
    List<Booking> findFutureBookingsOwner(LocalDateTime data, int id, Sort sort);

    List<Booking> findByStatusAndItemOwnerIdOrderByStartDesc(BookingStatus status, int id);
}