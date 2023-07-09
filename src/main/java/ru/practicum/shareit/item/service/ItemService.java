package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.item.dto.ResponseItemDTO;

@Service
public interface ItemService {

    ResponseItemDTO createItem(RequestItemDTO dto, Long userId);
}
