package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ResponseUserDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @Email
    private String email;
}
