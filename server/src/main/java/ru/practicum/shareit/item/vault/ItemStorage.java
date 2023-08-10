package ru.practicum.shareit.item.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemStorage {

    @Autowired
    private final ItemRepository repository;

    public Item createItem(Item newItem) {
        log.trace("LEVEL: Storage. METHOD: createItem. INPUT: " + newItem);
        try {
            if (newItem.getId() != null) {
                throw new ShareItIvanlidEntity("New item must not have id");
            }
            return saveIfValid(newItem);
        } catch (DataAccessException ex) {
            throw new ShareItSQLExecutionFailed("Failed to save new item due to: " + ex.getMessage());
        }
    }

    public Item updateItem(Item newItem) {
        log.trace("LEVEL: Storage. METHOD: createItem. INPUT: " + newItem);
        try {
            if (newItem.getId() == null) {
                throw new ShareItIvanlidEntity("Existing item must not have id");
            }
            return saveIfValid(newItem);
        } catch (DataAccessException ex) {
            throw new ShareItSQLExecutionFailed("Failed to update item due to: " + ex.getMessage());
        }
    }

    public Item readItemById(Long itemId) {
        return repository.findById(itemId).orElseThrow(
                () -> new ShareItEntityNotFound("Item with id = " + itemId + " not found")
        );
    }

    public List<Item> readUserItems(User user, int from, int size) {
        return repository.findUserItems(user.getId(), from, size);
    }

    public List<Item> readRequestItems(ItemRequest request) {
        return repository.findRequestItems(request.getId());
    }

    public List<Item> searchItems(String searchString, int from, int size) {
        if (searchString.isBlank()) {
            return List.of();
        }
        return repository.seachForItems("%" + searchString + "%", from, size);
    }

    private Item saveIfValid(Item savedItem) {
        try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            if (!validator.validate(savedItem).isEmpty()) {
                throw new ShareItIvanlidEntity("User parametrs invalid: "
                        + validator.validate(savedItem).stream().map(
                        violation -> "Поле "
                                + violation.getPropertyPath().toString()
                                + " " + violation.getMessage()
                ).collect(Collectors.joining(", ")));
            }
            return repository.save(savedItem);
        }
    }
}
