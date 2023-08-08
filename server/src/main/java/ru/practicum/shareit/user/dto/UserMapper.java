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

    public static ResponseUserDTO toDto(User user) {
        return new ResponseUserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User updateFromDto(User user, RequestUserDTO dto) {
        return new User(
                user.getId(),
                dto.getName() != null && !user.getName().equals(dto.getName()) ? dto.getName() : user.getName(),
                dto.getEmail() != null && !user.getEmail().equals(dto.getEmail()) ? dto.getEmail() : user.getEmail()
        );
    }
}
