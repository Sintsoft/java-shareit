package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItValueAlreadyTaken;

import javax.validation.ConstraintViolationException;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserServiceWIthDBRepo implements UserService{

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
            checkEmailAvaliable(newUser.getEmail());
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
        }
    }

    private void checkEmailAvaliable(String email) {
        log.trace("Level: SERVICE. Call of checkEmailAvaliable. Payload: " + email);
        if (!userRepo.findUserByEmail(email).isEmpty()) {
            log.debug(email + " is already taken");
            throw new ShareItValueAlreadyTaken("Email " + email + " is already taken!");
        }
        log.debug(email + " is free to use");
    }
}
