package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService {

    private int idIterator = 1;

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.fromDto(userDto);
        user.setId(idIterator++);
        return UserMapper.toDto(user);
    }
}
