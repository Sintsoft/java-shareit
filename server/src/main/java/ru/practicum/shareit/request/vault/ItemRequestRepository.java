package ru.practicum.shareit.request.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query(nativeQuery = true,
           value = "select r.* from requests r where r.requestor_id = :userId " +
                   "order by r.id limit :size offset :from")
    List<ItemRequest> getUserRequestsPage(@Param("userId") Long userId,
                                          @Param("from") int from,
                                          @Param("size") int size);

    @Query(nativeQuery = true,
           value = "select r.* from requests r where r.requestor_id != :userId " +
                    "order by r.id limit :size offset :from")
    List<ItemRequest> getAllRequestsPage(@Param("userId") Long userId,
                                         @Param("from") int from,
                                         @Param("size") int size);
}
