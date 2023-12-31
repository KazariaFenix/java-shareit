package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingWithItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    private Long id;
    @NotBlank
    @Size(max = 32)
    private String name;
    @NotBlank
    @Size(max = 256)
    private String description;
    @NotNull
    private Boolean available;
    private BookingWithItemDto nextBooking;
    private BookingWithItemDto lastBooking;
    private List<CommentDto> comments;
}
