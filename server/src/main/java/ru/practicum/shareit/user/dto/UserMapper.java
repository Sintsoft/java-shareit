package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public abstract class UserMapper {

    public static User fromDto(RequestUserDTO dto) {
        return new User(
                null,
                dto.getName(),
                dto.getEmail()
        );
    }

    public static NestedUserDTO toNestedDTO(User user) {
        return new NestedUserDTO(user.getId());
    }

    public static ResponseUserDTO toDto(User user) {
        return new ResponseUserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

}