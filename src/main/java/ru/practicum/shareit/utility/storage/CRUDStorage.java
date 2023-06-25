package ru.practicum.shareit.utility.storage;

import ru.practicum.shareit.utility.Entity;
import ru.practicum.shareit.utility.exception.NotFoundException;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class CRUDStorage<T extends Entity> {

    private int iterator = 1;

    private final Map<Integer, T> storage = new TreeMap<>();

    public T create(T subject) {
        if (subject.getId() != null) {
            throw new ValidationException("Wrong method");
        }
        subject.setId(iterator++);
        storage.put(subject.getId(), subject);
        return subject;
    }

    public T read(int id) {
        if (!storage.containsKey(id)) {
            throw new NotFoundException("not found");
        }
        return storage.get(id);
    }

    public List<T> readAll() {
        return List.copyOf(storage.values());
    }

    public T update(T subject) {
        if (subject.getId() == null) {
            throw new ValidationException("Null id");
        }
        if (!storage.containsKey(subject.getId())) {
            throw new NotFoundException("Not found");
        }
        storage.put(subject.getId(), subject);
        return subject;
    }

    public void delete(int id) {
        if (!storage.containsKey(id)) {
            throw new NotFoundException("not found");
        }
        storage.remove(id);
    }


    public Stream<T> stream() {
        return storage.values().stream();
    }
}
