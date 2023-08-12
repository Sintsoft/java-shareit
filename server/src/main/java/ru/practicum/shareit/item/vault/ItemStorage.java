package ru.practicum.shareit.item.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItInvalidEntity;

import javax.transaction.Transactional;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemStorage {

    @Autowired
    private final ItemRepository repository;

    @Transactional
    public Item createItem(@Validated Item item) {
        if (item.getId() != null) {
            throw new ShareItInvalidEntity("Invalid item");
        }
        return repository.save(item);
    }

    @Transactional
    public Item updateItem(@Validated Item item) {
        if (item.getId() == null) {
            throw new ShareItInvalidEntity("Item id cannot be null");
        }
        return repository.save(item);
    }

    @Transactional
    public Item getItem(@Positive Long itemId) {
        log.trace("Level: STORAGE. Call of loadItem. Payload: " + itemId);
        return repository.findById(itemId)
                .orElseThrow(
                        () -> {
                            throw new ShareItEntityNotFound("No item with id = " + itemId);
                        });
    }

    public List<Item> getUserItems(User user, int from, int size) {
        return repository.getUserItems(user.getId(), from, size);
    }

    @Transactional
    public List<Item> searchItemsPage(String searchString, int from, int size) {
        List<Item> items = repository.searchItemsPage(searchString, from, size);
        return items;
    }

    @Transactional
    public List<Item> getRequestItems(ItemRequest request) {
        return repository.getRequestItems(request.getId());
    }

}

