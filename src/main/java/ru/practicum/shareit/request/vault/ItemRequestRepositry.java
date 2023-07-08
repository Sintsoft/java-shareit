package ru.practicum.shareit.request.vault;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepositry extends JpaRepository<ItemRequest, Long> {

    @Query("select r From ItemRequest r where r.requestor = ?1 order by r.id ASC")
    List<ItemRequest> getUserRequests(User user);

    @Query("select r From ItemRequest r where r.requestor != ?1 order by r.id ASC")
    Page<ItemRequest> getOtherUserRequests(User user, Pageable pageable);
}
