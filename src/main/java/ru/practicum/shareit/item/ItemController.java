package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    @Autowired
    private final ItemService service;

    @PostMapping
    public ItemDto postItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Integer userId) {
        log.debug("Call POST /items. Header user-id = " + userId);
        itemDto.setOwner(userId);
        itemDto = service.addItem(itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Integer userId,
            @PathVariable Integer itemId) {
            itemDto.setOwner(userId);
            itemDto = service.updateItem(itemId, itemDto);
        return itemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable @Positive Integer itemId) {
        return service.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) @Positive Integer userId) {
        if (userId == null) return service.getAllItems();
        return service.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(
            @RequestParam @NotNull String text) {
        return service.searchItem(text);
    }

}
