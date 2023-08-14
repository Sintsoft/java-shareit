package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDTO;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.vault.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class UserServiceOnDBImplementation implements UserService {

    @Autowired
    private final UserStorage userStorage;


    @Override
    public ResponseUserDTO createUser(RequestUserDTO dto) {
        return UserMapper.toDto(
                userStorage.createUser(UserMapper.fromDto(dto))
        );
    }

    @Override
    public ResponseUserDTO updateUser(RequestUserDTO dto, Long userId) {
        return UserMapper.toDto(
                userStorage.updateUser(
                        userStorage.getUser(userId).updateFromDto(dto))
        );
    }

    @Override
    public ResponseUserDTO getUser(Long userId) {
        return UserMapper.toDto(userStorage.getUser(userId));
    }

    @Override
    public List<ResponseUserDTO> getUsersPage(int from, int size) {
        return userStorage.getUsersPage(from, size)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }
}
