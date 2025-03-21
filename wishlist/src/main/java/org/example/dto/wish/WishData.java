package org.example.dto.wish;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WishData {
    private UUID id;

    @NotNull(message = "name of wish can't be null")
    @NotBlank(message = "name of wish can't be empty")
    private String name;
    private String description;
    private String link;
    private double price;
}
