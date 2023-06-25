package ru.practicum.shareit.comment.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInnerException;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItSQLException;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentStorage {

    @Autowired
    private final CommentRepository repository;

    @Transactional
    public Comment createComment(Comment comment) {
        try {
            return repository.save(comment);
        } catch (DataIntegrityViolationException ex) {
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        }
    }

    @Transactional
    public List<Comment> getItemComments(Item item) {
        try {
            List<Comment> comments = repository.getItemComments(item);
            return comments;
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        } catch (Exception ex) {
            log.debug("We got inner error");
            throw new ShareItInnerException("Something bad happened, We are working to fix it.");
        }
    }
}
