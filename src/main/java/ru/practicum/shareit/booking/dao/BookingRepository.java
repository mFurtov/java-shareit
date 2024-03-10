package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.modul.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository  extends JpaRepository<Booking, Integer> {
    @Query("select b from Booking b join b.item i where b.id = ?2 and (b.booker.id = ?1 or i.owner.id = ?1)")
    Booking searchBooking(int requestId, int bookingId);
    @Query("SELECT b FROM Booking b WHERE b.start <= ?1 AND b.end >= ?1 and b.booker.id = ?2 and b.status IN ('WAITING', 'APPROVED') order by b.start desc")
    List<Booking>  findCurrentBookings(LocalDateTime data, int id);
    List<Booking>  findByBookerIdOrderByStartDesc(int id);
    @Query("SELECT b FROM Booking b WHERE b.end <= ?1 and b.booker.id = ?2 and b.status IN ('WAITING', 'APPROVED') order by b.start desc")
    List<Booking> findPastBookings(LocalDateTime data, int id);
    @Query("SELECT b FROM Booking b WHERE b.start >= ?1 and b.booker.id = ?2 and b.status IN ('WAITING', 'APPROVED') order by b.start desc")
    List<Booking> findFutureBookings(LocalDateTime data, int id);
    List<Booking> findByStatusAndBookerIdOrderByStartDesc(BookingStatus status,int id);
    List<Booking> findByItemIdAndItemOwnerIdOrderByStart(int itemId, int ownerId);
    List<Booking> findByItemOwnerIdOrderByStart(int ownerId);
    List<Booking> findByItemIdOrderByStart(int itemId);






    @Query("SELECT b FROM Booking b WHERE b.start <= ?1 AND b.end >= ?1 and b.item.owner.id = ?2 and b.status IN ('WAITING', 'APPROVED') order by b.start desc")
    List<Booking>  findCurrentBookingsOwner(LocalDateTime data, int id);
    List<Booking>  findByItemOwnerIdOrderByStartDesc(int id);
    @Query("SELECT b FROM Booking b WHERE b.end <= ?1 and b.item.owner.id = ?2 and b.status IN ('WAITING', 'APPROVED') order by b.start desc")
    List<Booking> findPastBookingsOwner(LocalDateTime data, int id);
    @Query("SELECT b FROM Booking b WHERE b.start >= ?1 and b.item.owner.id = ?2 and b.status IN ('WAITING', 'APPROVED') order by b.start desc")
    List<Booking> findFutureBookingsOwner(LocalDateTime data, int id);
    List<Booking> findByStatusAndItemOwnerIdOrderByStartDesc(BookingStatus status,int id);
}