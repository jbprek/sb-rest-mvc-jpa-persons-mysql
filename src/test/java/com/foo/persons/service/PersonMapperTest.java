package com.foo.persons.service;

import com.foo.persons.db.PersonEntity;
import com.foo.persons.rest.PersonDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonMapperTest {
    PersonMapper mapper = new PersonMapperImpl();

    @DisplayName("Test entity to DTO mapping, method PersonMapper#entityToDTO")
    @Test
    void testEntityToDTO() {

        var entity = PersonEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2000, 1, 1))
                .country("USA")
                .build();
        var dto = mapper.toDto(entity);
        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(entity.getId(), dto.id()),
                () -> assertEquals(entity.getFirstName(), dto.firstName()),
                () -> assertEquals(entity.getLastName(), dto.lastName()),
                () -> assertEquals(entity.getBirthDate(), dto.birthDate()),
                () -> assertEquals(entity.getCountry(), dto.country())
        );
    }

    @DisplayName("Test list of entities to list of DTO mapping, method PersonMapper#entitiesToDTOs")
    @Test
    void testEntitiesToDTOs() {

        var entityList = List.of(
                PersonEntity.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .birthDate(LocalDate.of(2000, 1, 1))
                        .country("USA")
                        .build(),
                PersonEntity.builder()
                        .id(2L)
                        .firstName("Jane")
                        .lastName("Doe")
                        .birthDate(LocalDate.of(2000, 1, 1))
                        .country("USA")
                        .build()
        );

        var dtoList = mapper.toDTOs(entityList);
        assertAll(
                () -> assertNotNull(dtoList),
                () -> assertEquals(entityList.size(), dtoList.size())
        );
    }

    @DisplayName("Test DTO to Entity mapping, method PersonMapper#dtoToEntity")
    @Test
    void testDtoToEntity() {
        var dto = new PersonDto(null,
                "John", "" +
                "Doe",
                LocalDate.of(2000, 1, 1),
                "USA");


        var entity = mapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(entity),
                () -> assertEquals(dto.firstName(), entity.getFirstName()),
                () -> assertEquals(dto.lastName(), entity.getLastName()),
                () -> assertEquals(dto.birthDate(), entity.getBirthDate()),
                () -> assertEquals(dto.country(), entity.getCountry())
        );
    }

    @DisplayName("Test partial update of Entity from a DTO, method PersonMapper#updateEntity")
    @Test
    void testEntityUpdateFromDto() {
        var dto = new PersonDto(
                null,
                null,
                null,
                LocalDate.of(2001, 2, 1),
                null
        );

        var entity = PersonEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2000, 1, 1))
                .country("USA")
                .build();

        mapper.partialUpdate(dto, entity);
        assertAll(
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("John", entity.getFirstName()),
                () -> assertEquals("Doe", entity.getLastName()),
                () -> assertEquals("USA", entity.getCountry()),
                () -> assertEquals(LocalDate.of(2001, 2, 1), entity.getBirthDate())
        );
    }
}