package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.RequestCommentDTO;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postItem(@Positive Long userId, @Validated RequestItemDTO dto) {
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        if (dto.getAvailable() == null) {
            throw new ShareItIvanlidEntity("Set avability");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ShareItIvanlidEntity("Set item name");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new ShareItIvanlidEntity("Set item description");
        }
        return post("", userId, dto);
    }

    public ResponseEntity<Object> patchItem(RequestItemDTO dto,
                                            @Positive Long itemId,
                                            @Positive Long userId) {
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        if (itemId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        return patch("/" + itemId, userId, dto);
    }

    public ResponseEntity<Object> getItem(@Positive Long itemId, @Positive Long userId) {
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        if (itemId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getUserItems(@Positive Long userId,
                                               @Positive int from,
                                               @Positive int size) {
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        if (from < 0 || size < 1) {
            throw new ShareItEntityNotFound("Set correct pagination parameters");
        }
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> searchItems(String searchString,
                                              @Positive Long userId,
                                              @Positive int from,
                                              @Positive int size) {
        if (from < 0 || size < 1) {
            throw new ShareItEntityNotFound("Set correct pagination parameters");
        }
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        Map<String, Object> parameters = Map.of(
                "text", searchString,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> postComment(@Validated RequestCommentDTO dto,
                                              @Positive Long itemId,
                                              @Positive Long userId) {
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        if (itemId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        if (dto.getText() == null || dto.getText().isBlank()) {
            throw new ShareItIvanlidEntity("Set item description");
        }
        return post("/" + itemId + "/comment", userId, dto);
    }

}
