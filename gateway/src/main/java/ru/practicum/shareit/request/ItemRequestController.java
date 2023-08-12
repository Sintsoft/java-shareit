package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> postRequest(@RequestBody ItemRequestDto dto,
                                              @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemRequestClient.createRequest(userId, dto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                             @PathVariable @Positive Long requestId) {
        return itemRequestClient.getRequest(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId
                                             ) {
        return itemRequestClient.getUserRequests(userId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getAllRequests(userId, from, size);
    }
}
