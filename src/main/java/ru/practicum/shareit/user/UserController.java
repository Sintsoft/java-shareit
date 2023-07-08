package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {


    @Autowired
    private final UserService service;

    @PostMapping
    public ResponseUserDto postUser(@RequestBody RequestUserDTO dto) {
        return service.createUser(dto);
    }


    @PatchMapping("/{userId}")
    public ResponseUserDto patchUser(@RequestBody RequestUserDTO dto,
                                    @PathVariable Long userId) {
        return service.updateUser(dto, userId);
    }

    @GetMapping("/{userId}")
    public ResponseUserDto getUser(@PathVariable Long userId) {
        return service.findUserById(userId);
    }

    @GetMapping
    public List<ResponseUserDto> getUsers(@RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        return service.findAllUsers(from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
    }
}
