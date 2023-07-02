package ru.practicum.shareit.request.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemRequestStorage {

    @Autowired
    private final ItemRequestRepositry repositry;

    public ItemRequest createRequest(@Validated ItemRequest request) {

        try {

            return repositry.save(request);
        } catch (Exception ex) {
            throw new ShareItInvalidEntity("Failed to save");
        }
    }

    public List<ItemRequest> loadUserRequests(User user) {
        return repositry.getUserRequests(user);
    }

    public List<ItemRequest> loadAll(User user, int from, int size) {
        return repositry.getOtherUserRequests(user, PageRequest.of(from, size)).toList();
    }

    public ItemRequest loadRequest(Long requestId) {
        return repositry.findById(requestId).orElseThrow(() -> new ShareItEntityNotFound("Item request do not exists"));
    }
}
