package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDto;

@Service
public interface UserService {

    ResponseUserDto createUser(RequestUserDTO inputDTO);
}
