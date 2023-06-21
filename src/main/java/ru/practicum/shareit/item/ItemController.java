package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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
    ItemDto postItem(@Validated @RequestBody ItemDto itemDto,
                     @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + itemDto + " " + userId);
        return service.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    ItemDto patchItem(@Validated @RequestBody ItemDto itemDto,
                     @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                     @PathVariable Long itemId) {
        log.trace("Level: CONTROLLER. Call of patchItem. Payload: " + itemDto + " " + itemId + " " + userId);
        return service.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@PathVariable Long itemId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + itemId);
        return service.getItem(itemId);
    }

    @GetMapping
    List<ItemDto> getUserItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.trace("Level: CONTROLLER. Call of postItem. Payload: " + userId);
        return service.getUserItems(userId);
    }

    @GetMapping("/search")
    List<ItemDto> searchItems(@RequestParam(required = true, name = "text") String searchStr) {
        return service.searchItem(searchStr);
    }
}
