package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utility.storage.AppStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

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
            addItem.setOwner(storage.UserStorage.read(dto.getOwner()));
        }
        addItem = storage.ItemStorage.create(addItem);
        return ItemMapper.toDto(addItem);
    }

    @Override
    public ItemDto updateItem(Integer id, ItemDto dto) {
        Item itemForUpd = storage.ItemStorage.read(id);
        Item updItem = ItemMapper.fromDto(dto);
        updItem.setOwner(storage.UserStorage.read(dto.getOwner()));
        if (updItem.getName() != null && !updItem.getName().equals(itemForUpd.getName())) {
            itemForUpd.setName(updItem.getName());
        }
        if (updItem.getDescription() != null && !updItem.getDescription().equals(itemForUpd.getDescription())) {
            itemForUpd.setDescription(updItem.getDescription());
        }
        if (updItem.getAvailable() != null && !updItem.getAvailable().equals(itemForUpd.getAvailable())) {
            itemForUpd.setAvailable(updItem.getAvailable());
        }
        if (updItem.getOwner() != null && !updItem.getOwner().equals(itemForUpd.getOwner())) {
            itemForUpd.setOwner(updItem.getOwner());
        }
        itemForUpd = storage.ItemStorage.update(itemForUpd);
        return ItemMapper.toDto(itemForUpd);
    }

    @Override
    public ItemDto getItem(int dto) {
        return ItemMapper.toDto(storage.ItemStorage.read(dto));
    }

    @Override
    public List<ItemDto> getAllItems() {
        return storage.ItemStorage.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeItem(int id) {
        storage.ItemStorage.delete(id);
    }

    @Override
    public List<ItemDto> getUserItems(int userId) {
        return storage.ItemStorage.stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String searchString) {
        return storage.ItemStorage.stream().filter(item ->
                item.getName().toLowerCase().contains(searchString.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(searchString.toLowerCase()))
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
