package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestItemRequestDTO;
import ru.practicum.shareit.request.dto.ResponseItemRequestDTO;

import java.util.List;

@Service
public interface ItemRequestService {

    ResponseItemRequestDTO createRequest(RequestItemRequestDTO dto, Long userId);

    ResponseItemRequestDTO getRequest(Long requestId, Long userId);

    List<ResponseItemRequestDTO> getRequests(Long userId, int from, int size);

    List<ResponseItemRequestDTO> getUserRequests(Long userId, int from, int size);
}
