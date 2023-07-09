package ru.practicum.shareit.request.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemRequestStorage {

    @Autowired
    private final ItemRequestRepository repository;

    public ItemRequest readItemRequest(Long id) {
        return id == null ? null : repository.findById(id).orElseThrow(
                () -> new ShareItEntityNotFound("Item request with id = " + id + " not found")
        );
    }
}
