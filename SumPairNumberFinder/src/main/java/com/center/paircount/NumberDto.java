package com.center.paircount;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Data
@Getter
@Setter
public class NumberDto {

    @NotNull(message = "Target number is required and cannot be null.")
    @Positive(message = "Target number must be a positive integer.")
    private Integer targetNumber;

    @NotNull(message = "size number is required and cannot be null.")
    @Positive(message = "size number must be a positive integer.")
    private Integer numberListSize;

    @NotNull(message = "min number is required and cannot be null.")
    @Positive(message = "min number must be a positive integer.")
    private Integer minNumber;

    @NotNull(message = "max number is required and cannot be null.")
    @Positive(message = "max number must be a positive integer.")
    private Integer maxNumber;
}
