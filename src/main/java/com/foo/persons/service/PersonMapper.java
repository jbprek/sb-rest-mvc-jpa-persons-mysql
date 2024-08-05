package com.foo.persons.service;

import com.foo.persons.db.PersonEntity;
import com.foo.persons.rest.PersonDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Map DTOs to JPA Entities and vice-versa
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDto toDto(PersonEntity entity);

    List<PersonDto> toDTOs(Iterable<PersonEntity> entities);

    PersonEntity toEntity(PersonDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PersonEntity partialUpdate(PersonDto dto, @MappingTarget PersonEntity personEntity);
}

