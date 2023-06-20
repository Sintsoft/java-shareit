package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public interface UserService {

    UserDto createUser(UserDto dto);
}
