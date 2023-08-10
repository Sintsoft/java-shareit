package ru.practicum.shareit.request.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query(nativeQuery = true, value = "select r.* from requests r where r.user_id = ?1"+
            " order by r.id asc limit ?3 offset ?2")
    List<ItemRequest> findUserRequests(Long userId, int from, int size);

    @Query(nativeQuery = true, value = "select r.* from requests r"+
            " order by r.id asc limit ?3 offset ?2")
    List<ItemRequest> findAllPageable(int from, int size);
}
