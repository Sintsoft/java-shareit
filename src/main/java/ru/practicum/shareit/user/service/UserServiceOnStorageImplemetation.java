package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.vault.UserStorage;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class UserServiceOnStorageImplemetation implements UserService {

    @Autowired
    private final UserStorage userStorage;

    @Override
    public ResponseUserDto createUser(RequestUserDTO inputDTO) {
        log.trace("LEVEL: Service. METHOD: createUser. INPUT: " + inputDTO);
        return UserMapper.toDto(
                userStorage.createUser(
                        UserMapper.fromDto(inputDTO, null)));
    }
}
