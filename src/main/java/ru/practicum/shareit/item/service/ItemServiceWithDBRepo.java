package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.errorHandling.exceptions.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class ItemServiceWithDBRepo implements ItemService {

    @Autowired
    private final ItemRepository itemRepo;

    @Autowired
    private final UserRepository userRepo;

    @Override
    public ItemDto createItem(ItemDto dto, Long userId) {
        log.trace("Level: SERVICE. Call of createItem. Payload: " + dto);
        try {
            log.trace("Validating item");
            Item newItem = validateItem(ItemMapper.fromDto(dto));

            log.trace("Item is vaild. Validating user... ");
            newItem.setUser(checkUserExists(userId));

            log.trace("User is valid. Saving item ...");
            newItem = itemRepo.save(newItem);

            log.trace("Item saved");
            return ItemMapper.toDto(newItem);
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid item");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect item entity");
            throw new ShareItInvalidEntity("Invalid item");
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            if (ex.getMessage().contains("constraint [uq_user_email]")) {
                throw new ShareItValueAlreadyTaken("Email is already taken!");
            }
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        }
    }

    @Override
    public ItemDto updateItem(ItemDto dto, Long itemId, Long userId) {
        log.trace("Level: SERVICE. Call of createItem. Payload: " + dto);
        try {
            log.trace("Validating item exists");
            Item updatedItem = checkItemExists(itemId);

            log.trace("Item is vaild. Validating user... ");
            if (updatedItem.getUser().getId() != userId) {
                log.info("Incorrect item owner id.");
                throw new ShareItNotAllowedAction("Incorrect item owner id.");
            }
            User owner = checkUserExists(userId); // Проверим консистентность данных

            log.trace("User is valid. Saving item ...");
            updatedItem = ItemMapper.updateFromDto(updatedItem, dto);
            updatedItem = itemRepo.save(updatedItem);

            return ItemMapper.toDto(updatedItem);
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid item");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect item entity");
            throw new ShareItInvalidEntity("Invalid item");
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            if (ex.getMessage().contains("constraint [uq_user_email]")) {
                throw new ShareItValueAlreadyTaken("Email is already taken!");
            }
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        }
    }

    @Override
    public ItemDto getItem(Long itemId) {
        log.trace("Level: SERVICE. Call of getItem. Payload: " + itemId);
        return ItemMapper.toDto(checkItemExists(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        log.trace("Level: SERVICE. Call of getUserItems. Payload: " + userId);
        return itemRepo.findUserItems(checkUserExists(userId))
                .stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String searchString) {
        log.trace("Level: SERVICE. Method: searchItem. Payload: " + searchString);
        if (searchString.isEmpty() || searchString.isBlank()) {
            return List.of();
        }
        return itemRepo.findByNameOrDescriptionContainingIgnoreCase(searchString, searchString)
                .stream()
                .filter(i -> i.getAvailable() == true)
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private Item validateItem(Item item) {
        log.trace("Level: SERVICE. Call of validateItem. Payload: " + item);
        if (!Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(item).isEmpty()) {
            throw new ShareItInvalidEntity("Incorrect item");
        }
        return item;
    }

    private Item checkItemExists(Long itemId) {
        log.trace("Level: SERVICE. Call of checkItemExists. Payload: " + itemId);
        Optional<Item> item = itemRepo.findById(itemId);
        if (item.isEmpty()) {
            log.debug("No user with id = " + itemId);
            throw new ShareItEntityNotFound("No user with id = " + itemId);
        }
        return item.get();
    }

    private User checkUserExists(Long userId) {
        log.trace("Level: SERVICE. Call of checkUserExists. Payload: " + userId);
        if (userId == null) {
            log.debug("User id can not be null");
            throw new ShareItInvalidEntity("Set item owner in header");
        }
        Optional<User> owner = userRepo.findById(userId);
        if (owner.isEmpty()) {
            log.debug("No user with id = " + userId);
            throw new ShareItEntityNotFound("No user with id = " + userId);
        }
        return owner.get();
    }
}
