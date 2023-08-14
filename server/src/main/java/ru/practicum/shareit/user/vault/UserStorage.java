package ru.practicum.shareit.user.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;
import ru.practicum.shareit.utility.exceptions.ShareItUniqueValueCollision;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStorage {

    @Autowired
    private final UserRepoitory repository;

    @Transactional
    public User createUser(User newUser) {
        log.debug("LEVEL: Storage. METHOD: createUser. INPUT: " + newUser);
        try {
            return repository.save(newUser);
        } catch (DataAccessException ex) {
            log.info("SQL exception!");
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("constraint")) {
                log.info("Email collision during creation. Email - " + newUser.getEmail());
                throw new ShareItUniqueValueCollision("This email is already taken");
            }
            throw new ShareItSQLExecutionFailed("Failed to save new user due to: " + ex.getMessage());
        }
    }

    @Transactional
    public User updateUser(User updUser) {
        log.debug("LEVEL: Storage. METHOD: updateUser. INPUT: " + updUser);
        try {
            return repository.save(updUser);
        } catch (DataAccessException ex) {
            log.info("SQL exception!");
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("constraint")) {
                log.warn("Email collision during creation. Email - " + updUser.getEmail());
                throw new ShareItUniqueValueCollision("This email is already taken");
            }
            throw new ShareItSQLExecutionFailed("Failed to save new user due to: " + ex.getMessage());
        }
    }

    @Transactional
    public User getUser(Long userId) {
        log.debug("LEVEL: Storage. METHOD: updateUser. INPUT: " + userId);
        return repository.findById(userId)
                .orElseThrow(
                        () -> new ShareItEntityNotFound("User with id = " + userId + " not found"));
    }

    @Transactional
    public List<User> getUsersPage(int from, int size) {
        log.debug("LEVEL: Storage. METHOD: getUsersPage. INPUT: " + from + " " + size);
        return repository.getUsersPage(from, size);
    }

    @Transactional
    public void deleteUser(Long userId) {
        log.debug("LEVEL: Storage. METHOD: deleteUser. INPUT: " + userId);
        repository.delete(getUser(userId));
    }

}
