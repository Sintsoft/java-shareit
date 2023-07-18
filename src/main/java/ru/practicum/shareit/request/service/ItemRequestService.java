package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;

import java.util.List;

@Service
public interface ItemRequestService {

    ResponseItemRequestDto createRequest(RequestItemRequestDto dto, Long userId);

    List<ResponseItemRequestDto> getUserRequests(Long userId);

    List<ResponseItemRequestDto> getRequests(int from, int size, Long userId);

    ResponseItemRequestDto getRequest(Long requestId, long userId);
}
