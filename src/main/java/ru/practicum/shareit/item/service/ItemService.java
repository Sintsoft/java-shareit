package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.item.dto.ResponseItemDTO;

import java.util.List;

@Service
public interface ItemService {

    ResponseItemDTO createItem(RequestItemDTO dto, Long userId);

    ResponseItemDTO updateItem(RequestItemDTO dto, Long itemId, Long userId);

    ResponseItemDTO getItem(Long itemId);

    List<ResponseItemDTO> getUserItems(Long userId, int from, int size);

    List<ResponseItemDTO> searchItems(String searchString, int from, int size);
}
