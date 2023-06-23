package ru.practicum.shareit.user.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;

@Slf4j
public class UserMapper {

    private UserMapper() {
    }

    public static User fromDto(UserDto dto) {
        log.debug("Mapping User from UserDto object. UserDto = " + dto);
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail()
        );
    }

    public static User upateFromDto(UserDto dto, User oldUser) {
        log.debug("Updateing User: " + oldUser + " from UserDto object. UserDto = " + dto);
        return new User(
                oldUser.getId(),
                dto.getName() != null
                        && !dto.getName().equals(oldUser.getName())
                        ? dto.getName() : oldUser.getName(),
                dto.getEmail() != null
                        && !dto.getEmail().equals(oldUser.getEmail())
                        ? dto.getEmail() : oldUser.getEmail()
        );
    }

    public static UserDto toDto(User user) {
        log.debug("Mapping UserDto from User object. User = " + user);
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static NestedUserDto toNestedDto(User user) {
        log.debug("Mapping NestedUserDto from User object. User = " + user);
        return new NestedUserDto(
                user.getId()
        );
    }
}
