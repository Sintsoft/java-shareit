package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.utility.Entity;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class User extends Entity {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    public User(Integer id, String name, String email) {
        super(id);
        if (name == null) {
            throw new ValidationException("User name can nto be null");
        }
        this.name = name;
        if (email == null) {
            throw new ValidationException("User email can not be null");
        }else if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            throw new ValidationException("User email is not vaild");
        }
        this.email = email;
    }
}
