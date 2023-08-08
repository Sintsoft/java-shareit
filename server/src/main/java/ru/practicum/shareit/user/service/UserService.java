package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDTO;

import java.util.List;

@Service
public interface UserService {

    ResponseUserDTO createUser(RequestUserDTO inputDTO);

    ResponseUserDTO updateUser(RequestUserDTO inputDTO, Long userId);

    ResponseUserDTO findUserById(Long userId);

    List<ResponseUserDTO> findAllUsers(int from, int size);

    void deleteUser(Long userId);
}
