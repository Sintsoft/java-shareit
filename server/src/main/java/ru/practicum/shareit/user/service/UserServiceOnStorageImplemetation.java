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
public class UserServiceOnStorageImplemetation implements UserService {

    @Autowired
    private final UserStorage userStorage;

    @Override
    public ResponseUserDTO createUser(RequestUserDTO inputDTO) {
        log.trace("LEVEL: Service. METHOD: createUser. INPUT: " + inputDTO);
        return UserMapper.toDto(
                userStorage.createUser(
                        UserMapper.fromDto(inputDTO)));
    }

    @Override
    public ResponseUserDTO updateUser(RequestUserDTO inputDTO, Long userId) {
        return UserMapper.toDto(
                userStorage.updateUser(
                        UserMapper.updateFromDto(
                                userStorage.readUserById(userId), inputDTO)));
    }

    @Override
    public ResponseUserDTO findUserById(Long userId) {
        return UserMapper.toDto(userStorage.readUserById(userId));
    }

    @Override
    public List<ResponseUserDTO> findAllUsers(int from, int size) {
        return userStorage.readManyUsers(from, size).stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }

}
