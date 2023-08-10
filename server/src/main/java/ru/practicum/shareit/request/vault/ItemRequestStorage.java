package ru.practicum.shareit.request.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemRequestStorage {

    @Autowired
    private final ItemRequestRepository repository;

    public  ItemRequest crateItemRequest(ItemRequest newRequest) {
        log.trace("LEVEL: Storage. METHOD: crateItemRequest. INPUT: " + newRequest);
        try {
            if (newRequest.getId() != null) {
                throw new ShareItIvanlidEntity("New item request must not have id");
            }
            return repository.save(newRequest);
        } catch (DataAccessException ex) {
            throw new ShareItSQLExecutionFailed("Failed to save new item due to: " + ex.getMessage());
        }
    }

    public ItemRequest readItemRequest(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ShareItEntityNotFound("Item request with id = " + id + " not found")
        );
    }

    public List<ItemRequest> readUserRequests(User user, int from, int size) {
        return repository.findUserRequests(user.getId(), from, size);
    }

    public List<ItemRequest> readPageOfRequests(int from, int size) {
        return repository.findAllPageable(from, size);
    }

}
