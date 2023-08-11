package ru.practicum.shareit.user.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface UserRepoitory extends JpaRepository<User, Long> {

    List<User> findByEmail(String email);

    @Query(nativeQuery = true,
           value = "select u.* from users order by u.id limit :size offset :from")
    public List<User> getUsersPage(@Param("from") int from, @Param("size") int size);
}
