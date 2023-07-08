package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public abstract class UserMapper {


    public static User fromDto(RequestUserDTO dto, Long userId) {
        return new User(
                userId,
                dto.getName(),
                dto.getEmail()
        );
    }

    public static ResponseUserDto toDto(User user) {
        return new ResponseUserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
