package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
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
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User owner;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    @ToString.Exclude
    private ItemRequest request;

    public Item(String name, String description, Boolean available, User owner, ItemRequest itemRequest) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = itemRequest;

    }
}
