package ru.practicum.shareit.item.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;

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
        return saveToRepo(item);
    }

    @Transactional
    public Item updateItem(@Validated Item item) {
        if (item.getId() == null) {
            throw new ShareItInvalidEntity("Item id cannot be null");
        }
        return saveToRepo(item);
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
        return repository.searchItemsPage(searchString, from, size);
    }

    @Transactional
    public List<Item> getRequestItems(ItemRequest request) {
        return repository.getRequestItems(request.getId());
    }

    @Transactional
    private Item saveToRepo(Item item) {
        try {
            return repository.save(item);
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            throw new ShareItSQLExecutionFailed("Something bad happened, We are working to fix it.");
        }
    }
}

