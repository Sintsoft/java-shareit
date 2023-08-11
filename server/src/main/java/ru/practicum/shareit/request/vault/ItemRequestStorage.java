package ru.practicum.shareit.request.vault;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemRequestStorage {

    @Autowired
    private final ItemRequestRepository repository;

    @Transactional
    public ItemRequest createItemRequest(ItemRequest newRequest) {
        log.debug("LEVEL: Storage. METHOD: createItemRequest. INPUT: " + newRequest);
        try {
            return repository.save(newRequest);
        } catch (DataAccessException ex) {
            log.info("SQL exception!");
            throw new ShareItSQLExecutionFailed("Failed to save new user due to: " + ex.getMessage());
        }
    }

    @Transactional
    public ItemRequest getItemRequest(Long requestId) {
        log.debug("LEVEL: Storage. METHOD: createItemRequest. INPUT: " + requestId);
        return repository.findById(requestId)
                .orElseThrow(
                        () -> new ShareItEntityNotFound("Item request with id = " + requestId + " not found"));
    }

    @Transactional
    public List<ItemRequest> getUserRequestsPage(@NonNull User user, int from, int size) {
        log.debug("LEVEL: Storage. METHOD: createItemRequest. INPUT: " + user
                  + " " + from + " " + size);
        return repository.getUserRequestsPage(user.getId(), from, size);
    }

    @Transactional
    public List<ItemRequest> getAllRequestsPage(int from, int size) {
        log.debug("LEVEL: Storage. METHOD: createItemRequest. INPUT: "
                + " " + from + " " + size);
        return repository.getAllRequestsPage(from, size);
    }
}
