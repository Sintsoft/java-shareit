package ru.practicum.shareit.user.vault;

import ru.practicum.shareit.user.model.User;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmail(String email);
}
