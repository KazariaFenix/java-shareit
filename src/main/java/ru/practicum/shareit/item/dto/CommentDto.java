package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class CommentDto {
    @NotNull
    private long id;
    @NotNull
    @NotBlank
    private String text;
    @NotNull
    private String authorName;
    @NotNull
    private LocalDateTime created;
}
