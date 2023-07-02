package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    @Autowired
    private final ItemRequestService service;

    @PostMapping
    public ResponseItemRequestDto postRequest(@Validated @RequestBody RequestItemRequestDto dto,
                                              @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.createRequest(dto, userId);
    }

    @GetMapping
    public List<ResponseItemRequestDto> getUserRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ResponseItemRequestDto> getAllRequests(@RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.getRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseItemRequestDto getRequest(@PathVariable Long requestId,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.getRequest(requestId, userId);
    }
}
