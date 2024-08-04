package com.foo.app.service;

import com.foo.app.db.PersonEntity;
import com.foo.app.rest.PersonInDto;
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
        var dto = mapper.entityToDTO(entity);
        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(entity.getId(), dto.getId()),
                () -> assertEquals(entity.getFirstName(), dto.getFirstName()),
                () -> assertEquals(entity.getLastName(), dto.getLastName()),
                () -> assertEquals(entity.getBirthDate(), dto.getBirthDate()),
                () -> assertEquals(entity.getCountry(), dto.getCountry())
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

        var dtoList = mapper.entitiesToDTOs(entityList);
        assertAll(
                () -> assertNotNull(dtoList),
                () -> assertEquals(entityList.size(), dtoList.size())
        );
    }

    @DisplayName("Test DTO to Entity mapping, method PersonMapper#dtoToEntity")
    @Test
    void testDtoToEntity() {
        var dto = PersonInDto.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2000, 1, 1))
                .country("USA")
                .build();

        var entity = mapper.dtoToEntity(dto);

        assertAll(
                () -> assertNotNull(entity),
                () -> assertEquals(dto.getFirstName(), entity.getFirstName()),
                () -> assertEquals(dto.getLastName(), entity.getLastName()),
                () -> assertEquals(dto.getBirthDate(), entity.getBirthDate()),
                () -> assertEquals(dto.getCountry(), entity.getCountry())
        );
    }

    @DisplayName("Test partial update of Entity from a DTO, method PersonMapper#updateEntity")
    @Test
    void testEntityUpdateFromDto() {
        var dto = PersonInDto.builder()
                .birthDate(LocalDate.of(2001, 2, 1))
                .build();

        var entity = PersonEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2000, 1, 1))
                .country("USA")
                .build();

        mapper.updateEntity(dto, entity);
        assertAll(
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("John", entity.getFirstName()),
                () -> assertEquals("Doe", entity.getLastName()),
                () -> assertEquals("USA", entity.getCountry()),
                () -> assertEquals(LocalDate.of(2001, 2, 1), entity.getBirthDate())
        );
    }
}