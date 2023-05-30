package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.utility.exception.EntityCollisionExcption;
import ru.practicum.shareit.utility.storage.AppStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final AppStorage storage;

    @Override
    public UserDto addUser(UserDto dto) {
        if (dto.getEmail() == null || dto.getName() == null) {
            log.debug("Null user name or email");
            throw new ValidationException("Null feild on post");
        }
        validateUserMailIsUnoue(dto.getEmail());
        User adableUser = storage.UserStorage.create(UserMapper.fromDto(dto));
        log.debug("Added item id = " + adableUser.getId());
        return UserMapper.toDto(adableUser);
    }

    @Override
    public UserDto updateUser(Integer id, UserDto dto) {
        User updUser = storage.UserStorage.read(id);
        if (dto.getEmail() != null) {
            validateUserMailIsUnoue(dto.getEmail());
            updUser.setEmail(dto.getEmail());
        }
        if (dto.getName() != null) {
            updUser.setName(dto.getName());
        }
        storage.UserStorage.update(updUser);
        return UserMapper.toDto(updUser);
    }

    @Override
    public void removeUser(int id) {
        storage.UserStorage.delete(id);;
    }

    @Override
    public UserDto getUser(int id) {
        return UserMapper.toDto(storage.UserStorage.read(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return storage.UserStorage.stream()
                .map(UserMapper::toDto).collect(Collectors.toList());
    }

    private void validateUserMailIsUnoue(String email) {
        if (storage.UserStorage.stream()
                .anyMatch(o -> email
                        .toLowerCase().equals(
                                o.getEmail()
                        )
                )) {
            throw new EntityCollisionExcption("User with that email already exist");
        }
    }
}
