package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "items")
public class    Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "item_name")
    @NotBlank
    private String name;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "available")
    @NotNull
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    public Item updateFromDto(RequestItemDTO dto) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            this.name = dto.getName();
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            this.description = dto.getDescription();
        }
        if (dto.getAvailable() != null) {
            this.available = dto.getAvailable();
        }
        return this;
    }
}
