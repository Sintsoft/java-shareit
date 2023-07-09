package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.item.dto.ResponseItemDTO;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.user.vault.UserStorage;

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
                ))
        );
    }
}
