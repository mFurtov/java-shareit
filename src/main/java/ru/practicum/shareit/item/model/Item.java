package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.ToString;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @CollectionTable(name = "name")
    private String name;
    @CollectionTable(name = "description")
    private String description;
    @CollectionTable(name = "available")
    private Boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User owner;
    @Transient
    private ItemRequest request;

    public Item(String name, String description, Boolean available, User owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;

    }
}
