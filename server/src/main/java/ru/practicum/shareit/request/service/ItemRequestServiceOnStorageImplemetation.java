package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestItemRequestDTO;
import ru.practicum.shareit.request.dto.ResponseItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.vault.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceOnStorageImplemetation implements ItemRequestService {


    @Autowired
    private final ItemRequestStorage requestStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final ItemStorage itemStorage;

    @Override
    public ResponseItemRequestDTO createRequest(Long userId, RequestItemRequestDTO dto) {
        log.debug("createRequest");
        return ItemRequestMapper.toDto(
                requestStorage.crateItemRequest(
                        ItemRequestMapper.fromDto(
                                dto,
                                userStorage.readUserById(userId))
                ),
                List.of());
    }

    @Override
    public List<ResponseItemRequestDTO> getUserRequests(Long userId, int from, int size) {
        return requestStorage.readUserRequests(userStorage.readUserById(userId), from, size)
                .stream()
                .map(request ->
                        ItemRequestMapper.toDto(request, itemStorage.readRequestItems(request))
                ).collect(Collectors.toList());
    }

    @Override
    public List<ResponseItemRequestDTO> getRequests(Long userId, int from, int size) {
        log.trace("LEVEL: Service. METHOD: getRequests.");
        return requestStorage.readPageOfRequests(from, size)
                .stream()
                .map(request ->
                    ItemRequestMapper.toDto(request, itemStorage.readRequestItems(request))
                ).collect(Collectors.toList());
    }

    @Override
    public ResponseItemRequestDTO getRequest(Long requestId, long userId) {
        User user = userStorage.readUserById(userId);
        ItemRequest request = requestStorage.readItemRequest(requestId);
        return ItemRequestMapper.toDto(
                request,
                itemStorage.readRequestItems(request)
        );
    }
}
