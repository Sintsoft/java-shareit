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
import ru.practicum.shareit.user.vault.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class ItemRequestServicOnDBImplementation implements ItemRequestService {

    @Autowired
    private final ItemRequestStorage requestStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final ItemStorage itemStorage;


    @Override
    public ResponseItemRequestDTO createRequest(RequestItemRequestDTO dto, Long userId) {
        return ItemRequestMapper.toDTO(
                requestStorage.createItemRequest(
                        ItemRequestMapper.fromDTO(
                                dto,
                                userStorage.getUser(userId)
                        )
                ),
                List.of()
        );
    }

    @Override
    public ResponseItemRequestDTO getRequest(Long requestId, Long userId) {
        userStorage.getUser(userId);
        ItemRequest request = requestStorage.getItemRequest(requestId);
        return ItemRequestMapper.toDTO(
                request,
                itemStorage.getRequestItems(request)
        );
    }

    @Override
    public List<ResponseItemRequestDTO> getRequests(Long userId, int from, int size) {
        return requestStorage.getAllRequestsPage(userStorage.getUser(userId), from, size)
                .stream()
                .map(request -> ItemRequestMapper.toDTO(
                        request,
                        itemStorage.getRequestItems(request)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseItemRequestDTO> getUserRequests(Long userId, int from, int size) {
        return requestStorage.getUserRequestsPage(userStorage.getUser(userId), from, size)
                .stream()
                .map(request -> ItemRequestMapper.toDTO(
                        request,
                        itemStorage.getRequestItems(request)
                ))
                .collect(Collectors.toList());
    }
}
