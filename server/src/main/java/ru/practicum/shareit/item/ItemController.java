package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.NestedCommentDTO;
import ru.practicum.shareit.comment.dto.RequestCommentDTO;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.item.dto.ResponseItemDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private final ItemService service;

    @PostMapping
    ResponseItemDTO postItem(@Validated @RequestBody RequestItemDTO itemDto,
                             @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + itemDto + " " + userId);
        return service.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    ResponseItemDTO patchItem(@Validated @RequestBody RequestItemDTO itemDto,
                              @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId) {
        log.trace("Level: CONTROLLER. Call of patchItem. Payload: " + itemDto + " " + itemId + " " + userId);
        return service.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    ResponseItemDTO getItem(@PathVariable Long itemId,
                            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + itemId);
        return service.getItem(itemId, userId);
    }

    @GetMapping
    List<ResponseItemDTO> getUserItems(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + userId);
        return service.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    List<ResponseItemDTO> searchItems(@RequestParam(required = true, name = "text") String searchStr,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        return service.serarchItems(searchStr, from, size);
    }

    @PostMapping("/{itemId}/comment")
    NestedCommentDTO postComment(@PathVariable Long itemId,
                                 @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @Validated @RequestBody RequestCommentDTO commentDto) {
        return service.commentItem(commentDto, itemId, userId);
    }
}
