package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.item.dto.ResponseItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.user.vault.UserStorage;
import ru.practicum.shareit.utility.exceptions.ShareItUnalllowedAction;
import ru.practicum.shareit.utility.validation.RequestParameterValidation;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class ItemServiceOnStorageImplemetation implements ItemService {

    @Autowired
    private final ItemStorage itemStorage;

    @Autowired
    private final UserStorage userStorage;


    @Override
    public ResponseItemDTO createItem(RequestItemDTO dto, Long userId) {
        return ItemMapper.toDto(
                itemStorage.createItem(ItemMapper.fromDto(
                        dto, userStorage.readUserById(userId), null
                )));
    }

    @Override
    public ResponseItemDTO updateItem(RequestItemDTO dto, Long itemId, Long userId) {
        Item oldItem = itemStorage.readItemById(itemId);
        if (!oldItem.getUser().getId().equals(userId)) {
            throw new ShareItUnalllowedAction("User with id = " + userId + " not allowed to edit this edit");
        }
        return ItemMapper.toDto(
                itemStorage.updateItem(oldItem.updateFromDto(dto)));
    }

    @Override
    public ResponseItemDTO getItem(Long itemId) {
        return ItemMapper.toDto(itemStorage.readItemById(itemId));
    }

    @Override
    public List<ResponseItemDTO> getUserItems(Long userId, int from, int size) {
        RequestParameterValidation.paginationParameterValidation(from, size);
        return itemStorage.readUserItems(
                userStorage.readUserById(userId),
                from, size).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }



    @Override
    public List<ResponseItemDTO> searchItems(String searchString, int from, int size) {
        RequestParameterValidation.paginationParameterValidation(from, size);
        return itemStorage.searchItems(searchString, from, size)
                .stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }

}
