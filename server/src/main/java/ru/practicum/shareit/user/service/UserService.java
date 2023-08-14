package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDTO;

import java.util.List;

public interface UserService {

   ResponseUserDTO createUser(RequestUserDTO dto);

   ResponseUserDTO updateUser(RequestUserDTO dto, Long userId);

   ResponseUserDTO getUser(Long userId);

   List<ResponseUserDTO> getUsersPage(int from, int size);

   void deleteUser(Long userId);
}
