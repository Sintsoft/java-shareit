package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.RequestUserDTO;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "user_name", nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public User updateFromDto(RequestUserDTO dto) {
        this.name = dto.getName() == null || dto.getName().isBlank() ? this.name : dto.getName();
        this.email = dto.getEmail() == null || dto.getEmail().isBlank() ? this.email : dto.getEmail();
        return this;
    }
}
