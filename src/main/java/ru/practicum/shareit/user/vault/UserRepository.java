package ru.practicum.shareit.user.vault;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmail(String email);

    @Query(nativeQuery = true, value = "select u.* from users u limit ?2 offset ?1")
    List<User> findAllFromSize(int from, int size);
}
