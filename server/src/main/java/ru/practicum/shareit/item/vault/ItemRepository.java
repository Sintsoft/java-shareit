package ru.practicum.shareit.item.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;


import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "select i.* from items i where i.user_id = ?1" +
            " order by i.id asc limit ?3 offset ?2", nativeQuery = true)
    List<Item> findUserItems(Long user, int from, int size);

    List<Item> findByNameOrDescriptionContainingIgnoreCase(String nameSearchString, String descriptionSearchString);

    @Query(value = "select i.* from items i where i.request_id = ?1" +
            " order by i.id", nativeQuery = true)
    List<Item> findRequestItems(Long requestIde);

    @Query(value = "select i.* from items i where i.item_name ILIKE :search OR i.description ILIKE :search" +
            " order by i.id asc limit :size offset :from", nativeQuery = true)
    List<Item> seachForItems(@Param("search") String searchString, @Param("from") int from, @Param("size") int size);
}
