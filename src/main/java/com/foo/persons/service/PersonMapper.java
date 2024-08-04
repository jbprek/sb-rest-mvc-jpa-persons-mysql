package com.foo.persons.service;

import com.foo.persons.db.PersonEntity;
import com.foo.persons.rest.PersonInDto;
import com.foo.persons.rest.PersonOutDto;
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

    PersonOutDto toDto(PersonEntity entity);

    List<PersonOutDto> toDTOs(Iterable<PersonEntity> entities);

    PersonEntity toEntity(PersonInDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PersonEntity partialUpdate(PersonInDto dto, @MappingTarget PersonEntity personEntity);
}

