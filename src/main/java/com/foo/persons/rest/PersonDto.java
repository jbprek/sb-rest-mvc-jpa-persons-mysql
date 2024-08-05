package com.foo.persons.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

import java.time.LocalDate;

public record PersonDto(
        @NotNull(groups = ValidateOnUpdate.class,
                message = "id can't be null")
        Long id,

        @NotBlank(groups = ValidateOnCreate.class,
                message = "first name can't be blank")
        @Size(max = 255,
                message = "first name must be between 1 and 255 characters")
        String firstName,

        @NotBlank(groups = ValidateOnCreate.class,
                message = "last name can't be blank")
        @Size(max = 255,
                message = "last name must be between 1 and 255 characters")
        String lastName,

        @NotNull(groups = ValidateOnCreate.class,
                message = "birth date can't be null")
        @Past(message = "birth date must be in the past")
        LocalDate birthDate,

        @NotNull(groups = ValidateOnCreate.class,
                message = "country code can't be null")
        @Size(min = 3, max = 3,
                message = "Country code must be 3 characters long")
        String country
) {
}
