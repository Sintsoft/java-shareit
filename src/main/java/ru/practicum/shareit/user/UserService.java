package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {

    UserDto addUser(UserDto dto);

    UserDto updateUser(Integer id, UserDto dto);

    void removeUser(int id);

    UserDto getUser(int id);

    List<UserDto> getAllUsers();
}
