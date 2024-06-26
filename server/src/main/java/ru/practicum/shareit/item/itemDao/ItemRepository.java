package ru.practicum.shareit.item.itemDao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByOwnerId(int ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND (?1 <> '') AND i.available = true")
    List<Item> search(String text, Pageable pageable);

    List<Item> findByRequestIdIn(List<Integer> id);
}
