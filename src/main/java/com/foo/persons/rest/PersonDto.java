package com.foo.persons.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PersonDto(
        Long id,

        @NotBlank
        @Size(max = 255,
                message = "first name must be between 1 and 255 characters")
        String firstName,

        @NotBlank
        @Size(max = 255,
                message = "last name must be between 1 and 255 characters")
        String lastName,

        @NotNull
        @Past
        LocalDate birthDate,

        @NotNull
        @Size(min = 3, max = 3,
                message = "Country code must be 3 characters long")
        String country
) {
}
