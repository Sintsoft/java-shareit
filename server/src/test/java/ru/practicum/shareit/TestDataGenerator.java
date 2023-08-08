package ru.practicum.shareit;

import ru.practicum.shareit.item.dto.NestedItemDTO;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.item.dto.ResponseItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestItemRequestDTO;
import ru.practicum.shareit.request.dto.ResponseItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDTO;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;


public abstract class TestDataGenerator {

    /*
    *   User test data generation block
    */

    public static User generateTestUser(Long id) {
        return new User(
                id,
                "user" + id,
                "user" + id + "@email.com"
        );
    }

    public static RequestUserDTO generateTestRequestUserDTO(long id) {
        return new RequestUserDTO(
                "user" + id,
                "user" + id + "@email.com"
        );
    }

    public static ResponseUserDTO generateTestResponseUserDTO(long id) {
        return new ResponseUserDTO(
                id,
                "user" + id,
                "user" + id + "@email.com"
        );
    }

    /*
    * Item test data deneration block
    */

    public static Item generateTestItem(long id, long ownerId, Long requestId, Long requestorId) {
        return new Item(
                id,
                "item" + id,
                "item" + id + " description",
                true,
                generateTestUser(ownerId),
                requestId != null ? generateTestItemRequest(requestId, requestorId) : null
        );
    }

    public static RequestItemDTO generateTestRequestItemDTO(long id, Long requestId) {
        return new RequestItemDTO(
                "item" + id,
                "item" + id + " description",
                true,
                requestId
        );
    }

    public static ResponseItemDTO generateTestResponseItemDTO(long id, Long requestId) {
        return new ResponseItemDTO(
                id,
                "item" + id,
                "item" + id + " description",
                true,
                requestId
        );
    }

    public static NestedItemDTO generateTestNestedItemDTO(long id) {
        return new NestedItemDTO(
                id,
                "item" + id,
                "item" + id + " description"
        );
    }


    /*
    * Item request data generation block
    */

    public static ItemRequest generateTestItemRequest(long id, long creatorId) {
        return new ItemRequest(
                id,
                "request" + id + " description",
                LocalDateTime.now(),
                generateTestUser(creatorId)
        );
    }

    public static RequestItemRequestDTO generateTestRequestItemRequest(Long id) {
        return new RequestItemRequestDTO("request" + id + " description");
    }

    public ResponseItemRequestDTO generateTestResponseItemRequest(Long id, Long itemId) {
        return new ResponseItemRequestDTO(
                id,
                "request" + id + " description",
                LocalDateTime.now(),
                List.of(generateTestNestedItemDTO(itemId))
        );
    }

}
