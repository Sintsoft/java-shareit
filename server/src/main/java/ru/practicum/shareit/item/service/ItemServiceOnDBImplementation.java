package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.vault.BookingStorage;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.dto.NestedCommentDTO;
import ru.practicum.shareit.comment.dto.RequestCommentDTO;
import ru.practicum.shareit.comment.vault.CommentStorage;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.item.dto.ResponseItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.request.vault.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserStorage;
import ru.practicum.shareit.utility.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItUnalllowedAction;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class ItemServiceOnDBImplementation implements ItemService {

    @Autowired
    private final ItemRequestStorage requestStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final ItemStorage itemStorage;

    @Autowired
    private final BookingStorage bookingStorage;

    @Autowired
    private final CommentStorage commentStorage;


    @Override
    public ResponseItemDTO createItem(RequestItemDTO dto, Long userId) {
        return ItemMapper.toDTO(
                itemStorage.createItem(
                    ItemMapper.fromDTO(
                        dto,
                        userStorage.getUser(userId),
                        dto.getRequestId() == null ? null : requestStorage.getItemRequest(dto.getRequestId()))
                    ), null, null, List.of());
    }

    @Override
    public ResponseItemDTO updateItem(RequestItemDTO dto, Long itemId, Long userId) {
        Item item = itemStorage.getItem(itemId);
        if (!item.getUser().getId().equals(userId)) {
            throw new ShareItUnalllowedAction("User " + userId + " can't update item " + itemId);
        }
        return ItemMapper.toDTO(
                itemStorage.updateItem(
                        item.updateFromDto(dto)
                ),
                bookingStorage.getItemLastBooking(item),
                bookingStorage.getItemNextBooking(item),
                commentStorage.getItemComments(item));
    }

    @Override
    public ResponseItemDTO getItem(Long itemId, Long userId) {
        Item item = itemStorage.getItem(itemId);
        return ItemMapper.toDTO(
                item,
                userId.equals(item.getUser().getId()) ? bookingStorage.getItemLastBooking(item) : null,
                userId.equals(item.getUser().getId()) ? bookingStorage.getItemNextBooking(item) : null,
                commentStorage.getItemComments(item));
    }

    @Override
    public List<ResponseItemDTO> getUserItems(Long userId, int from, int size) {
        return itemStorage.getUserItems(
                userStorage.getUser(userId), from, size)
                .stream()
                .map(item -> ItemMapper.toDTO(
                        item,
                        userId.equals(item.getUser().getId()) ? bookingStorage.getItemLastBooking(item) : null,
                        userId.equals(item.getUser().getId()) ? bookingStorage.getItemNextBooking(item) : null,
                        commentStorage.getItemComments(item)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseItemDTO> serarchItems(String searchString, Long userId, int from, int size) {
        if (searchString.isBlank()) {
            return List.of();
        }
        return itemStorage.searchItemsPage(searchString, from, size)
                .stream()
                .map(item -> ItemMapper.toDTO(
                        item,
                        userId.equals(item.getUser().getId()) ? bookingStorage.getItemLastBooking(item) : null,
                        userId.equals(item.getUser().getId()) ? bookingStorage.getItemNextBooking(item) : null,
                        commentStorage.getItemComments(item)))
                .collect(Collectors.toList());
    }

    @Override
    public NestedCommentDTO commentItem(RequestCommentDTO dto, Long itemId, Long userId) {
        User user = userStorage.getUser(userId);
        Item item = itemStorage.getItem(itemId);
        if (bookingStorage.userNotBookedItem(user, item)) {
            throw new ShareItInvalidEntity("This user can't comment item");
        }
        return CommentMapper.toNestedDTO(
                commentStorage.createComment(
                        CommentMapper.fromDTO(dto, item, user)));
    }
}
