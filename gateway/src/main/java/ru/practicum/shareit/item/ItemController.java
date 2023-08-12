package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.RequestCommentDTO;
import ru.practicum.shareit.item.dto.RequestItemDTO;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient client;

    @PostMapping
    ResponseEntity<Object> postItem(@Validated @RequestBody RequestItemDTO itemDto,
                                    @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + itemDto + " " + userId);
        return client.postItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<Object> patchItem(@Validated @RequestBody RequestItemDTO itemDto,
                              @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId) {
        log.trace("Level: CONTROLLER. Call of patchItem. Payload: " + itemDto + " " + itemId + " " + userId);
        return client.patchItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    ResponseEntity<Object> getItem(@PathVariable Long itemId,
                            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + itemId);
        return client.getItem(itemId, userId);
    }

    @GetMapping
    ResponseEntity<Object> getUserItems(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + userId);
        return client.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    ResponseEntity<Object> searchItems(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                       @RequestParam(required = true, name = "text") String searchStr,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return client.searchItems(searchStr, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> postComment(@PathVariable Long itemId,
                                 @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @Validated @RequestBody RequestCommentDTO commentDto) {
        return client.postComment(commentDto, itemId, userId);
    }
}
