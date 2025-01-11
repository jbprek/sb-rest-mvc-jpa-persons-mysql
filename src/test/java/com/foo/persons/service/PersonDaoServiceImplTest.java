package com.foo.persons.service;

import com.foo.persons.db.PersonEntity;
import com.foo.persons.db.PersonEntityRepository;
import com.foo.persons.rest.PersonDto;
import com.foo.persons.service.exception.PersonDaoException;
import com.foo.persons.service.exception.PersonDaoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonDaoServiceImplTest {

    @Mock
    private PersonMapper mapper;

    @Mock
    private PersonEntityRepository repository;

    @InjectMocks
    private PersonDaoServiceImpl service;

    private PersonDto personDto;
    private PersonEntity personEntity;

    @BeforeEach
    void setUp() {
        personDto = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");

        personEntity = new PersonEntity();
        personEntity.setId(1L);
        personEntity.setFirstName("John");
        personEntity.setLastName("Doe");
        personEntity.setBirthDate(LocalDate.of(1990, 1, 1));
        personEntity.setCountry("USA");
    }

    @Test
    void createPerson_success() {
        when(mapper.toEntity(any(PersonDto.class))).thenReturn(personEntity);
        when(repository.save(any(PersonEntity.class))).thenReturn(personEntity);
        when(mapper.toDto(any(PersonEntity.class))).thenReturn(personDto);

        PersonDto result = service.createPerson(personDto);

        assertNotNull(result);
        assertEquals(personDto.id(), result.id());
        assertEquals(personDto.firstName(), result.firstName());
        assertEquals(personDto.lastName(), result.lastName());
        assertEquals(personDto.birthDate(), result.birthDate());
        assertEquals(personDto.country(), result.country());

        verify(mapper, times(1)).toEntity(any(PersonDto.class));
        verify(repository, times(1)).save(any(PersonEntity.class));
        verify(mapper, times(1)).toDto(any(PersonEntity.class));
    }

    @Test
    void createPerson_failure() {
        when(mapper.toEntity(any(PersonDto.class))).thenReturn(personEntity);
        when(repository.save(any(PersonEntity.class))).thenThrow(new RuntimeException("Database error"));

        PersonDaoException exception = assertThrows(PersonDaoException.class, () -> service.createPerson(personDto));

        assertEquals("Failed to create Person: " + personDto, exception.getMessage());

        verify(mapper, times(1)).toEntity(any(PersonDto.class));
        verify(repository, times(1)).save(any(PersonEntity.class));
    }

    @Test
    void getPerson_success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        when(mapper.toDto(any(PersonEntity.class))).thenReturn(personDto);

        PersonDto result = service.getPerson(1L);

        assertNotNull(result);
        assertEquals(personDto.id(), result.id());
        assertEquals(personDto.firstName(), result.firstName());
        assertEquals(personDto.lastName(), result.lastName());
        assertEquals(personDto.birthDate(), result.birthDate());
        assertEquals(personDto.country(), result.country());

        verify(repository, times(1)).findById(anyLong());
        verify(mapper, times(1)).toDto(any(PersonEntity.class));
    }

    @Test
    void getPerson_notFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        PersonDaoNotFoundException exception = assertThrows(PersonDaoNotFoundException.class, () -> service.getPerson(1L));

        assertEquals("Person Not found id: 1", exception.getMessage());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getAll_success() {
        when(repository.findAll()).thenReturn(List.of(personEntity));
        when(mapper.toDTOs(anyList())).thenReturn(List.of(personDto));

        List<PersonDto> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(personDto.id(), result.getFirst().id());
        assertEquals(personDto.firstName(), result.getFirst().firstName());
        assertEquals(personDto.lastName(), result.getFirst().lastName());
        assertEquals(personDto.birthDate(), result.getFirst().birthDate());
        assertEquals(personDto.country(), result.getFirst().country());

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDTOs(anyList());
    }

    @Test
    void updatePerson_success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        when(mapper.partialUpdate(any(PersonDto.class), any(PersonEntity.class))).thenReturn(personEntity);
        when(repository.save(any(PersonEntity.class))).thenReturn(personEntity);
        when(mapper.toDto(any(PersonEntity.class))).thenReturn(personDto);

        PersonDto result = service.updatePerson(personDto);

        assertNotNull(result);
        assertEquals(personDto.id(), result.id());
        assertEquals(personDto.firstName(), result.firstName());
        assertEquals(personDto.lastName(), result.lastName());
        assertEquals(personDto.birthDate(), result.birthDate());
        assertEquals(personDto.country(), result.country());

        verify(repository, times(1)).findById(anyLong());
        verify(mapper, times(1)).partialUpdate(any(PersonDto.class), any(PersonEntity.class));
        verify(repository, times(1)).save(any(PersonEntity.class));
        verify(mapper, times(1)).toDto(any(PersonEntity.class));
    }

    @Test
    void updatePerson_failure() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        when(mapper.partialUpdate(any(PersonDto.class), any(PersonEntity.class))).thenReturn(personEntity);
        when(repository.save(any(PersonEntity.class))).thenThrow(new RuntimeException("Database error"));

        PersonDaoException exception = assertThrows(PersonDaoException.class, () -> service.updatePerson(personDto));

        assertEquals("Failed to create Person: " + personDto, exception.getMessage());

        verify(repository, times(1)).findById(anyLong());
        verify(mapper, times(1)).partialUpdate(any(PersonDto.class), any(PersonEntity.class));
        verify(repository, times(1)).save(any(PersonEntity.class));
    }

    @Test
    void deletePerson_success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        doNothing().when(repository).delete(any(PersonEntity.class));

        service.deletePerson(1L);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(PersonEntity.class));
    }

    @Test
    void deletePerson_notFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        PersonDaoNotFoundException exception = assertThrows(PersonDaoNotFoundException.class, () -> service.deletePerson(1L));

        assertEquals("Person Not found id: 1", exception.getMessage());

        verify(repository, times(1)).findById(anyLong());
    }
}