package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utility.exception.UnpermittedAction;
import ru.practicum.shareit.utility.storage.AppStorage;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final AppStorage storage;

    @Override
    public ItemDto addItem(ItemDto dto) {
        if (
                dto.getName() == null || dto.getName().isBlank()
                || dto.getDescription() == null
                || dto.getAvailable() == null) {
            log.debug("Null item name");
            throw new ValidationException("Item name can not be null");
        }
        Item addItem = ItemMapper.fromDto(dto);
        log.debug("Added item id = " + addItem.getId());
        if (dto.getOwner() != null) {
            addItem.setOwner(storage.userStorage.read(dto.getOwner()));
        }
        addItem = storage.itemStorage.create(addItem);
        return ItemMapper.toDto(addItem);
    }

    @Override
    public ItemDto updateItem(Integer id, ItemDto dto) {
        Item itemForUpd = storage.itemStorage.read(id);
        if (dto.getOwner() != null && !storage.userStorage.read(dto.getOwner()).equals(itemForUpd.getOwner())) {
            throw new UnpermittedAction("User change is not allowed");
        }
        if (dto.getName() != null && !dto.getName().equals(itemForUpd.getName())) {
            itemForUpd.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().equals(itemForUpd.getDescription())) {
            itemForUpd.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null && !dto.getAvailable().equals(itemForUpd.getAvailable())) {
            itemForUpd.setAvailable(dto.getAvailable());
        }
        itemForUpd = storage.itemStorage.update(itemForUpd);
        return ItemMapper.toDto(itemForUpd);
    }

    @Override
    public ItemDto getItem(int dto) {
        return ItemMapper.toDto(storage.itemStorage.read(dto));
    }

    @Override
    public List<ItemDto> getAllItems() {
        return storage.itemStorage.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeItem(int id) {
        storage.itemStorage.delete(id);
    }

    @Override
    public List<ItemDto> getUserItems(int userId) {
        return storage.itemStorage.stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String searchString) {
        if (searchString.isBlank() || searchString.isEmpty()) {
            return new ArrayList<ItemDto>();
        }
        return storage.itemStorage.stream().filter(item ->
                (item.getName().toLowerCase().contains(searchString.toLowerCase())
                        || item.getDescription().toLowerCase().contains(searchString.toLowerCase())
                ) && item.getAvailable())
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
