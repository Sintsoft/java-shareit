package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.vault.ItemRequestStorage;
import ru.practicum.shareit.user.vault.UserStorage;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class IremRequestServiceWithDBRepo implements ItemRequestService {

    @Autowired
    private final ItemRequestStorage itemRequestStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final ItemStorage itemStorage;

    @Override
    public ResponseItemRequestDto createRequest(RequestItemRequestDto dto, Long userId) {
        log.trace("Level: SERVICE. Call of createItem. Payload: " + dto + ",  " + userId);
        try {
            if (dto.getDescription() == null || dto.getDescription().isBlank()) {
                throw new ShareItInvalidEntity("Fill description");
            }
            return ItemRequestMapper.toDto(
                    itemRequestStorage.createRequest(
                            ItemRequestMapper.fromDto(dto, userStorage.loadUser(userId))),
                    List.of()
            );
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect item entity");
            throw new ShareItInvalidEntity("Invalid item");
        }
    }

    @Override
    public List<ResponseItemRequestDto> getUserRequests(Long userId) {
        return itemRequestStorage.loadUserRequests(userStorage.loadUser(userId))
                .stream()
                .map(request -> ItemRequestMapper.toDto(
                        request,
                        itemStorage.loadRequestItems(request)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseItemRequestDto> getRequests(int from, int size, Long userId) {
        return itemRequestStorage.loadAll(userStorage.loadUser(userId), from,size)
                .stream()
                .map(request -> ItemRequestMapper.toDto(request, itemStorage.loadRequestItems(request)))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseItemRequestDto getRequest(Long requestId, long userId) {
        userStorage.loadUser(userId);
        ItemRequest request = itemRequestStorage.loadRequest(requestId);
        return ItemRequestMapper.toDto(request, itemStorage.loadRequestItems(request));
    }
}
