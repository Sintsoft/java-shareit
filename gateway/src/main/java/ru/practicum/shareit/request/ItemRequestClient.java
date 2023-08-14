package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;

import javax.validation.constraints.Positive;
import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createRequest(@Positive Long userId, ItemRequestDto dto) {
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new ShareItIvanlidEntity("Null fields not allowed");
        }
        return post("", userId, dto);
    }

    public ResponseEntity<Object> getRequest(@Positive Long userId, @Positive Long requestId) {
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        if (requestId < 1) {
            throw new ShareItEntityNotFound("Item request id must be positive");
        }
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAllRequests(@Positive Long userId, @Positive int from, @Positive int size) {
        if (from < 0 || size < 1) {
            throw new ShareItIvanlidEntity("Set correct pagination parameters");
        }
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getUserRequests(@Positive Long userId, @Positive int from, @Positive int size) {
        if (from < 0 || size < 1) {
            throw new ShareItIvanlidEntity("Set correct pagination parameters");
        }
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }


}
