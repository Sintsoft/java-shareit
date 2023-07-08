package ru.practicum.shareit.item.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ResponseItemDTO {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @NonNull
    private Boolean available;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long requestId;
}
