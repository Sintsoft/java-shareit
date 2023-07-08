package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDto;

import java.util.List;

@Service
public interface UserService {

    ResponseUserDto createUser(RequestUserDTO inputDTO);

    ResponseUserDto updateUser(RequestUserDTO inputDTO, Long userId);

    ResponseUserDto findUserById(Long userId);

    List<ResponseUserDto> findAllUsers(int from, int size);

    void deleteUser(Long userId);
}
