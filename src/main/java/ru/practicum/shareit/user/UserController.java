package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService service;

    @PostMapping
    public UserDto postUser(@Valid @RequestBody UserDto userDto) {
        return service.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUser(
            @Valid @RequestBody UserDto userDto,
            @PathVariable int userId) {
        return service.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable int userId) {
        return service.getUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return service.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        service.removeUser(userId);
    }
}
