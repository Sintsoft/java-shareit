package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public interface ItemService {

    ItemDto createItem(ItemDto dto, Long userId);

    ItemDto updateItem(ItemDto dto, Long itemId, Long userId);

    ItemDto getItem(Long itemId);

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> searchItem(String searchString);

}
