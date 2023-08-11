package ru.practicum.shareit.item.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(nativeQuery = true,
           value = "select i.* from items i where i.user_id = :userId " +
                   "order by i.id asc limit :size offset :from")
    List<Item> getUserItems(@Param("userId") Long userId,
                            @Param("from") int from,
                            @Param("size") int size);

    List<Item> findByNameOrDescriptionContainingIgnoreCase(String nameSearchString, String descriptionSearchString);

    @Query(nativeQuery = true,
           value = "select i.* from items i where i.item_name like concat('%', :searchString ,'%') " +
                   "order by i.id asc limit :size offset :from")
    List<Item> searchItemsPage(@Param("searchString") String searchString,
                               @Param("from") int from,
                               @Param("size") int size);


    @Query(nativeQuery = true,
            value = "select i.* from items i where i.request_id = :requestId " +
                    "order by i.id asc")
    List<Item> getRequestItems(@Param("requestId") Long requestId);
}
