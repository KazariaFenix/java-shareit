package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
public class ItemShort {
    private Long id;
    @NotBlank
    @Size(max = 32)
    private String name;
    @NotBlank
    @Size(max = 256)
    private String description;
    @NotNull
    private Boolean available;
}
