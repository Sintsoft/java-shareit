package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public interface UserService {

    UserDto createUser(UserDto dto);

    UserDto updateUser(UserDto dto, Long userId);

    UserDto getUser(Long userId);

    List<UserDto> getAllUsers();

    void removeUser(Long userId);

//    User loadUser(Long userId);
}
