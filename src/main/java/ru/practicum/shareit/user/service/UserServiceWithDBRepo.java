package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItSQLException;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItValueAlreadyTaken;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserServiceWithDBRepo implements UserService{

    @Autowired
    private final UserRepository userRepo;

    @Override
    public UserDto createUser(UserDto dto) {
        log.trace("Level: SERVICE. Call of createUser. Payload: " + dto);
        try {
            if (dto.getId() != null) {
                log.debug("Dto.id is not null. Need to use update method.");
                throw new ShareItInvalidEntity("Can not create User with id.");
            }
            log.trace("Trying to create user enity from dto.");
            User newUser = UserMapper.fromDto(dto);
            log.trace("User is valid to use. Saving user.");
            newUser = userRepo.save(newUser);
            log.debug("User saved. New user id = " + newUser.getId());
            return UserMapper.toDto(newUser);
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid user");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect user entity");
            throw new ShareItInvalidEntity("Invalid user");
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            if (ex.getMessage().contains("constraint [uq_user_email]")) {
                throw new ShareItValueAlreadyTaken("Email is already taken!");
            }
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        }
    }

    @Override
    public UserDto updateUser(UserDto dto, Long userId) {
        log.trace("Level: SERVICE. Call of updateUser. Payload: DTO = " + dto + ", User.id = " + userId);
        try {
            User updatedUser = UserMapper.upateFromDto(dto, checkUserExists(userId));
            updatedUser = userRepo.save(updatedUser);
            return UserMapper.toDto(updatedUser);
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid user");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect user entity");
            throw new ShareItInvalidEntity("Invalid user");
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            if (ex.getMessage().contains("constraint [uq_user_email]")) {
                throw new ShareItValueAlreadyTaken("Email is already taken!");
            }
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        }
    }

    @Override
    public UserDto getUser(Long userId) {
        log.trace("Level: SERVICE. Call of getUser. Payload: " + userId);
        return UserMapper.toDto(checkUserExists(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.trace("Level: SERVICE. Call of getAllUsers.");
        return userRepo.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeUser(Long userId) {
        log.trace("Level: SERVICE. Call of getUser. Payload: " + userId);
        userRepo.delete(checkUserExists(userId));
    }

    private User checkUserExists(Long userId) {
        Optional<User> checkUser = userRepo.findById(userId);
        if (checkUser.isEmpty()) {
            throw new ShareItEntityNotFound("User with id = " + userId + " not found");
        }
        return checkUser.get();
    }
}
