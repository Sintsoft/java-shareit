package ru.practicum.shareit.item.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItSQLException;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemStorage {

    @Autowired
    private final ItemRepository repository;

    public Item createItem(@Validated Item item) {
        if (item.getId() != null) {
            throw new ShareItInvalidEntity("Invalid item");
        }
        return saveToRepo(item);
    }

    public Item updateItem(@Validated Item item) {
        if (item.getId() == null) {
            throw new ShareItInvalidEntity("Item id cannot be null");
        }
        return saveToRepo(item);
    }

    public Item loadItem(@Positive Long itemId) {
        log.trace("Level: STORAGE. Call of loadItem. Payload: " + itemId);
        Optional<Item> item = repository.findById(itemId);
        if (item.isEmpty()) {
            log.debug("No user with id = " + itemId);
            throw new ShareItEntityNotFound("No user with id = " + itemId);
        }
        return item.get();
    }

    public List<Item> loadUserItems(User user) {
        return repository.findUserItems(user);
    }

    public List<Item> searchForItems(String searchString) {
        return repository.findByNameOrDescriptionContainingIgnoreCase(searchString, searchString);
    }

    private Item saveToRepo(Item item) {
        try {
            return repository.save(item);
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        }
    }
}
