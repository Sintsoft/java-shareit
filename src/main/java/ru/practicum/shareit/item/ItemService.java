package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto dto);

    ItemDto updateItem(Integer id, ItemDto dto);

    ItemDto getItem(int dto);

    List<ItemDto> getAllItems();

    void removeItem(int id);

    List<ItemDto> getUserItems(int userId);

    List<ItemDto> searchItem(String searchString);
}
