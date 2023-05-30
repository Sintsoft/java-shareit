package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.utility.exception.EntityCollisionExcption;
import ru.practicum.shareit.utility.exception.NotFoundException;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl2 implements UserService {

    private int idIterator = 1;
    private final Map<Integer, User> memStorage = new TreeMap<>();

    @Override
    public UserDto addUser(UserDto dto) {
        if (dto.getEmail() == null || dto.getName() == null) {
            throw new ValidationException("Null feild on post");
        }
        User adableUser = UserMapper.fromDto(dto);
        validateUserMailIsUnoue(adableUser.getEmail());
        adableUser.setId(idIterator++);
        memStorage.put(adableUser.getId(), adableUser);
        return UserMapper.toDto(adableUser);
    }

    @Override
    public UserDto updateUser(Integer id, UserDto dto) {
        if (!memStorage.containsKey(id)) {
            throw new NotFoundException("USer not found");
        }
        User updUser = memStorage.get(id);
        if (dto.getEmail() != null) {
            validateUserMailIsUnoue(dto.getEmail());
            updUser.setEmail(dto.getEmail());
        }
        if (dto.getName() != null) {
            updUser.setName(dto.getName());
        }
        return UserMapper.toDto(updUser);
    }

    @Override
    public void removeUser(int id) {
        if (!memStorage.containsKey(id)) {
            throw new NotFoundException("USer not found");
        }
        memStorage.remove(id);
    }

    @Override
    public UserDto getUser(int id) {
        if (!memStorage.containsKey(id)) {
            throw new NotFoundException("USer not found");
        }
        return UserMapper.toDto(memStorage.get(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return memStorage.values().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    private void validateUserMailIsUnoue(String email) {
        if (memStorage.values().stream()
                .anyMatch(o -> email
                        .toLowerCase().equals(
                                o.getEmail()
                        )
                )) {
            throw new EntityCollisionExcption("User with that email already exist");
        }
    }

}
