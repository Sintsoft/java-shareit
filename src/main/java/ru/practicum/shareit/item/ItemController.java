package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.NestedCommentDto;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
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
    ResponseItemDto postItem(@Validated @RequestBody RequestItemDto itemDto,
                             @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + itemDto + " " + userId);
        return service.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    ResponseItemDto patchItem(@Validated @RequestBody RequestItemDto itemDto,
                              @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId) {
        log.trace("Level: CONTROLLER. Call of patchItem. Payload: " + itemDto + " " + itemId + " " + userId);
        return service.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    ResponseItemDto getItem(@PathVariable Long itemId,
                            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + itemId);
        return service.getItem(itemId, userId);
    }

    @GetMapping
    List<ResponseItemDto> getUserItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + userId);
        return service.getUserItems(userId);
    }

    @GetMapping("/search")
    List<ResponseItemDto> searchItems(@RequestParam(required = true, name = "text") String searchStr) {
        return service.searchItem(searchStr);
    }

    @PostMapping("/{itemId}/comment")
    NestedCommentDto postComment(@PathVariable Long itemId,
                                 @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @Validated @RequestBody RequestCommentDto commentDto) {
        return service.postComment(commentDto, itemId, userId);
    }
}
