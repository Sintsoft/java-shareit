package ru.practicum.shareit.user;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    <S extends User> S save(@Validated S entity);

    @Query("select u from User u where u.email = ?1")
    List<User> findUserByEmail(String email);
}
