package com.foo.app.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class PersonOutDto {

    Long id;
    String firstName;
    String lastName;
    LocalDate birthDate;
    String country;

}
