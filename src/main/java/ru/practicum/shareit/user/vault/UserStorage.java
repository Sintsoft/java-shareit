package ru.practicum.shareit.user.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItSQLException;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItValueAlreadyTaken;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStorage {

    @Autowired
    private final UserRepository repository;

    public User createUser(@Validated User user) {
        log.trace("Level: STORAGE. Call of createUser. Payload: " + user);
        if (user.getId() != null) {
            log.debug("Dto.id is not null. Need to use update method.");
            throw new ShareItInvalidEntity("Can not create User with id.");
        }
        return repository.save(user);
    }

    public User updateUser(@Validated User user) {
        log.trace("Level: STORAGE. Call of updateUser. Payload: " + user);
        saveToRepo(user);
    }

    public User loadUser(@Positive Long userId) {
        if (userId == null) {
            log.debug("User id can not be null");
            throw new ShareItInvalidEntity("Set item owner in header");
        }
        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ShareItEntityNotFound("User with id = " + userId + " not found");
        }
        return optionalUser.get();
    }

    public List<User> loadAllUsers() {
        return repository.findAll();
    }

    public void deleteUserById(@Positive Long userId) {
        repository.delete(loadUser(userId));
    }

    private User saveToRepo(User user) {
        try {
            return repository.save(user);
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            if (ex.getMessage().contains("constraint [uq_user_email]")) {
                throw new ShareItValueAlreadyTaken("Email is already taken!");
            }
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        }
    }
}
