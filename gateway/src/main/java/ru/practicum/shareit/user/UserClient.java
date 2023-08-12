package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.stream.Collectors;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(UserRequestDto dto) {
        if (dto.getEmail() == null || dto.getName() == null
                || dto.getName().isBlank()
                || dto.getEmail().isBlank()) {
            throw new ShareItIvanlidEntity("Null fields not allowed");
        }
        try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            if (!validator.validate(dto).isEmpty()) {
                throw new ShareItIvanlidEntity("User parametrs invalid: "
                        + validator.validate(dto).stream().map(
                        violation -> "Поле "
                                + violation.getPropertyPath().toString()
                                + " " + violation.getMessage()
                ).collect(Collectors.joining(", ")));
            }

        }
        return post("", dto);
    }

    public ResponseEntity<Object> updateUser(Long userid, UserRequestDto dto) {
        if (userid == null || userid < 1) {
            throw new ShareItIvanlidEntity("Existing user must have id");
        }
        return patch("/" + userid.toString(), dto);
    }

    public ResponseEntity<Object> getUser(Long userid) {
        return get("/" + userid);
    }

    public ResponseEntity<Object> getUsers(Integer from, Integer size) {
        String parrams = "?from=" + from.toString() + "&size=" + size.toString();
        return get(parrams);
    }

    public ResponseEntity<Object> deleteUser(Long userid) {
        return delete("/" + userid);
    }
}
