package ru.practicum.shareit.comment.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentStorage {

    @Autowired
    private final CommentRepository repository;

    @Transactional
    public Comment createComment(Comment comment) {
        try {
            return repository.save(comment);
        } catch (DataIntegrityViolationException ex) {
            throw new ShareItSQLExecutionFailed("Something bad happened, We are working to fix it.");
        }
    }

    @Transactional
    public List<Comment> getItemComments(Item item) {
        try {
            return repository.getItemComments(item);
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            throw new ShareItSQLExecutionFailed("Something bad happened, We are working to fix it.");
        } catch (Exception ex) {
            log.debug("We got inner error");
            throw new ShareItSQLExecutionFailed("Something bad happened, We are working to fix it.");
        }
    }
}
