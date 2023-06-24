package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.vault.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserStorage;
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
    private final ItemStorage itemStorage;

    @Autowired
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(ItemDto dto, Long userId) {
        log.trace("Level: SERVICE. Call of createItem. Payload: " + dto);
        try {
            log.trace("Parsing item");
            Item newItem = ItemMapper.fromDto(dto);

            log.trace("Item is vaild. Validating user... ");
            newItem.setUser(userStorage.loadUser(userId));

            log.trace("User is valid. Saving item ...");
            return ItemMapper.toDto(itemStorage.createItem(newItem));
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid item");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect item entity");
            throw new ShareItInvalidEntity("Invalid item");
        }
    }

    @Override
    public ItemDto updateItem(ItemDto dto, Long itemId, Long userId) {
        log.trace("Level: SERVICE. Call of createItem. Payload: " + dto);
        try {
            log.trace("Validating item exists");
            Item itemToUpd = itemStorage.loadItem(itemId);

            log.trace("Item is vaild. Validating user... ");
            if (itemToUpd.getUser().getId() != userId) {
                log.info("Incorrect item owner id.");
                throw new ShareItNotAllowedAction("Incorrect item owner id.");
            }
            User owner = userStorage.loadUser(userId); // Проверим консистентность данных

            log.trace("User is valid. Saving item ...");

            return ItemMapper.toDto(
                    itemStorage.updateItem(
                            ItemMapper.updateFromDto(itemToUpd, dto)));
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid item");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect item entity");
            throw new ShareItInvalidEntity("Invalid item");
        }
    }

    @Override
    public ItemDto getItem(Long itemId) {
        log.trace("Level: SERVICE. Call of getItem. Payload: " + itemId);
        return ItemMapper.toDto(itemStorage.loadItem(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        log.trace("Level: SERVICE. Call of getUserItems. Payload: " + userId);
        return itemStorage.loadUserItems(userStorage.loadUser(userId))
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
        return itemStorage.searchForItems(searchString)
                .stream()
                .filter(i -> i.getAvailable() == true)
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

}
