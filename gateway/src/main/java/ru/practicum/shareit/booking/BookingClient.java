package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        if (from < 0 || size < 1) {
            throw new ShareItIvanlidEntity("Set correct pagination parameters");
        }
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }


    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
        if (requestDto.getStart() == null
                || requestDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ShareItIvanlidEntity("Set correct booking start time");
        }
        if (requestDto.getEnd() == null) {
            throw new ShareItIvanlidEntity("Set correct booking end time");
        }
        if (requestDto.getEnd().isBefore(requestDto.getStart())
            || requestDto.getEnd().isEqual(requestDto.getStart())) {
            throw new ShareItIvanlidEntity("Set correct booking time");
        }
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> patchApprove(long bookingId, Boolean approved, long userId) {
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        if (bookingId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        return patch("/" + bookingId + "?approved=" + approved.toString(), userId);
    }

    public ResponseEntity<Object> getUserItemsBookings(long userId, BookingState state, Integer from, Integer size) {
        if (from < 0 || size < 1) {
            throw new ShareItIvanlidEntity("Set correct pagination parameters");
        }
        if (userId < 1) {
            throw new ShareItEntityNotFound("User id must be positive");
        }
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}