package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private final UserService service;

    @PostMapping
    public UserDto postUser(@Validated @RequestBody UserDto dto) {
        log.trace("Level: CONTROLLER. Call of postUser. Payload: " + dto);
        return service.createUser(dto);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUser(
            @Validated @RequestBody UserDto dto,
            @PathVariable Long userId) {
        log.trace("Level: CONTROLLER. Call of patchUser. Payload: " + dto + " " + userId);
        return service.updateUser(dto, userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.trace("Level: CONTROLLER. Call of postUser. Payload: " + userId);
        return service.getUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.trace("Level: CONTROLLER. Call of deleteUser.");
        return service.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.trace("Level: CONTROLLER. Call of deleteUser. Payload: " + userId);
        service.removeUser(userId);
    }
}
