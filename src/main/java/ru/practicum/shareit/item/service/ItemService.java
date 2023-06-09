package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.dto.NestedCommentDto;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;

import java.util.List;

@Service
public interface ItemService {

    ResponseItemDto createItem(RequestItemDto dto, Long userId);

    ResponseItemDto updateItem(RequestItemDto dto, Long itemId, Long userId);

    ResponseItemDto getItem(Long itemId, Long userId);

    List<ResponseItemDto> getUserItems(Long userId);

    List<ResponseItemDto> searchItem(String searchString);

    NestedCommentDto postComment(RequestCommentDto commentDto, Long itemId, Long userId);
}
