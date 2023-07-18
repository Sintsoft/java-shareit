package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingRequestStatus;
import ru.practicum.shareit.booking.vault.BookingStorage;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.dto.NestedCommentDto;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.vault.CommentStorage;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.request.vault.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserStorage;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItNotAllowedAction;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class ItemServiceWithDBRepo implements ItemService {

    @Autowired
    private final ItemStorage itemStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final BookingStorage bookingStorage;

    @Autowired
    private final CommentStorage commentStorage;

    @Autowired
    private final ItemRequestStorage itemRequestStorage;

    @Override
    public ResponseItemDto createItem(RequestItemDto dto, Long userId) {
        log.trace("Level: SERVICE. Call of createItem. Payload: " + dto);
        try {
            log.trace("Parsing item");
            Item newItem = ItemMapper.fromDto(dto);
            if (dto.getRequestId() != null) {
                log.trace("Checking request...");
                newItem.setRequest(itemRequestStorage.loadRequest(dto.getRequestId()));
            }

            log.trace("Item is vaild. Validating user... ");
            newItem.setUser(userStorage.loadUser(userId));

            log.trace("User is valid. Saving item ...");
            return ItemMapper.toDto(itemStorage.createItem(newItem), null, null, List.of());
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid item");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect item entity");
            throw new ShareItInvalidEntity("Invalid item");
        }
    }

    @Override
    public ResponseItemDto updateItem(RequestItemDto dto, Long itemId, Long userId) {
        log.trace("Level: SERVICE. Call of createItem. Payload: " + dto);
        try {
            log.trace("Validating item exists");
            Item itemToUpd = itemStorage.loadItem(itemId);

            log.trace("Item is vaild. Validating user... ");
            if (!itemToUpd.getUser().getId().equals(userId)) {
                log.info("Incorrect item owner id.");
                throw new ShareItNotAllowedAction("Incorrect item owner id.");
            }
            userStorage.loadUser(userId); // Проверим консистентность данных

            log.trace("User is valid. Saving item ...");
            itemToUpd = ItemMapper.updateFromDto(itemToUpd, dto);
            return ItemMapper.toDto(
                    itemStorage.updateItem(
                        itemToUpd),
                        bookingStorage.loadItemLastBooking(itemToUpd),
                        bookingStorage.loadItemNextBooking(itemToUpd),
                        commentStorage.getItemComments(itemToUpd));
        } catch (NullPointerException ex) {
            log.info("Got null pointer exception.");
            throw new ShareItInvalidEntity("Invalid item");
        } catch (ConstraintViolationException ex) {
            log.info("Incorrect item entity");
            throw new ShareItInvalidEntity("Invalid item");
        }
    }

    @Override
    public ResponseItemDto getItem(Long itemId, Long userId) {
        log.trace("Level: SERVICE. Call of getItem. Payload: " + itemId);
        Item searchItem = itemStorage.loadItem(itemId);
        if (searchItem.getUser().getId().equals(userId)) {
            return ItemMapper.toDto(
                    searchItem,
                    bookingStorage.loadItemLastBooking(searchItem),
                    bookingStorage.loadItemNextBooking(searchItem),
                    commentStorage.getItemComments(searchItem));
        }
        return ItemMapper.toDto(
                searchItem, null, null, commentStorage.getItemComments(searchItem));
    }

    @Override
    public List<ResponseItemDto> getUserItems(Long userId) {
        log.trace("Level: SERVICE. Call of getUserItems. Payload: " + userId);
        return itemStorage.loadUserItems(userStorage.loadUser(userId))
                .stream()
                .map(
                        item -> ItemMapper.toDto(item,
                                bookingStorage.loadItemLastBooking(item),
                                bookingStorage.loadItemNextBooking(item),
                                commentStorage.getItemComments(item))
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseItemDto> searchItem(String searchString) {
        log.trace("Level: SERVICE. Method: searchItem. Payload: " + searchString);
        if (searchString.isEmpty() || searchString.isBlank()) {
            return List.of();
        }
        return itemStorage.searchForItems(searchString)
                .stream()
                .filter(item -> item.getAvailable())
                .map(
                        item -> ItemMapper.toDto(item,
                                bookingStorage.loadItemLastBooking(item),
                                bookingStorage.loadItemNextBooking(item),
                                commentStorage.getItemComments(item))
                )
                .collect(Collectors.toList());
    }

    @Override
    public NestedCommentDto postComment(RequestCommentDto commentDto, Long itemId, Long userId) {
        User user = userStorage.loadUser(userId);
        Item item = itemStorage.loadItem(itemId);
        if (bookingStorage.loadUserBookings(user, BookingRequestStatus.PAST, 0, Integer.MAX_VALUE)
                .noneMatch(matchItem -> matchItem.getItem().getId().equals(itemId))
            || commentDto.getText().isBlank()) {
            throw new ShareItInvalidEntity("This user can't comment item");
        }
        return CommentMapper.toNested(
                commentStorage.createComment(
                        CommentMapper.fromDto(
                                commentDto,
                                item,
                                user)));
    }

}
