package ru.practicum.shareit.item.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.user = ?1")
    List<Item> findUserItems(User user);

    List<Item> findByNameOrDescriptionContainingIgnoreCase(String nameSearchString, String descriptionSearchString);
}
