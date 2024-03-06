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
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private User owner;
    @Transient
    private ItemRequest request;
    public Item() {
    }
    public Item(int id, String name, String description, Boolean available, User owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;

    }
}
