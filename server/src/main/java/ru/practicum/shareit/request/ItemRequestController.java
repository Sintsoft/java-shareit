package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestItemRequestDTO;
import ru.practicum.shareit.request.dto.ResponseItemRequestDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    @Autowired
    private final ItemRequestService service;

    @PostMapping
    public ResponseItemRequestDTO postRequest(@Validated @RequestBody RequestItemRequestDTO dto,
                                              @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.createRequest(dto, userId);
    }

    @GetMapping
    public List<ResponseItemRequestDTO> getUserRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        return service.getUserRequests(userId, from, size);
    }

    @GetMapping("/all")
    public List<ResponseItemRequestDTO> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        return service.getRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseItemRequestDTO getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                       @PathVariable Long requestId) {
        return service.getRequest(requestId, userId);
    }
}
