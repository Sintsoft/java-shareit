package ru.practicum.shareit.item.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
            throw new ShareItSQLExecutionFailed("Failed to save new user due to: " + ex.getMessage());
        }
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
