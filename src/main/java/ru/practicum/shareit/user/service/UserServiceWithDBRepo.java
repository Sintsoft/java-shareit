package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserStorage;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserServiceWithDBRepo implements UserService {

    @Autowired
    private final UserStorage userStorage;

    @Override
    public UserDto createUser(UserDto dto) {
        log.trace("Level: SERVICE. Call of createUser. Payload: " + dto);
        try {
            return UserMapper.toDto(userStorage.createUser(UserMapper.fromDto(dto)));
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid user");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect user entity");
            throw new ShareItInvalidEntity("Invalid user");
        }
    }

    @Override
    public UserDto updateUser(UserDto dto, Long userId) {
        log.trace("Level: SERVICE. Call of updateUser. Payload: DTO = " + dto + ", User.id = " + userId);
        try {
            User userToUpd = userStorage.loadUser(userId);
            userToUpd = UserMapper.upateFromDto(dto, userToUpd);
            return UserMapper.toDto(userStorage.updateUser(userToUpd));
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid user");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect user entity");
            throw new ShareItInvalidEntity("Invalid user");
        }
    }

    @Override
    public UserDto getUser(Long userId) {
        log.trace("Level: SERVICE. Call of getUser. Payload: " + userId);
        return UserMapper.toDto(userStorage.loadUser(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.trace("Level: SERVICE. Call of getAllUsers.");
        return userStorage.loadAllUsers()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeUser(Long userId) {
        log.trace("Level: SERVICE. Call of getUser. Payload: " + userId);
        userStorage.deleteUserById(userId);
    }

}
