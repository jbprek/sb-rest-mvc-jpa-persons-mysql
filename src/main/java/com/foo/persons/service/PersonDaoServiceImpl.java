package com.foo.persons.service;

import com.foo.persons.db.PersonEntity;
import com.foo.persons.db.PersonEntityRepository;
import com.foo.persons.rest.PersonDto;
import com.foo.persons.service.exception.PersonDaoException;
import com.foo.persons.service.exception.PersonDaoNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class PersonDaoServiceImpl implements PersonDaoService {
    private final PersonMapper mapper;
    private final PersonEntityRepository repository;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PersonDto createPerson(PersonDto dto) {
        try {
            var entity = mapper.toEntity(dto);
            var resEntity = repository.save(entity);
            return mapper.toDto(resEntity);
        } catch (Exception e) {
            var msg = "Failed to create Person: " + dto;
            log.error("{}, reason: {}", msg, e.toString());
            throw new PersonDaoException(msg, e);
        }
    }


    @Override
    public PersonDto getPerson(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new PersonDaoNotFoundException("Person Not found id: " + id));
        return mapper.toDto(entity);
    }


    @Override
    public List<PersonDto> getAll() {
        return mapper.toDTOs(repository.findAll());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PersonDto patchPerson(Long id, PersonDto dto) {
        return update(id, dto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PersonDto updatePerson(PersonDto dto) {
        return update(dto.id(), dto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deletePerson(Long id) {
        var entity = getEntity(id);
        repository.delete(entity);
    }

    private PersonEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PersonDaoNotFoundException("Person Not found id: " + id));
    }

    private PersonDto update(Long id, PersonDto dto) {
        try {
            var entity = getEntity(id);
            mapper.partialUpdate(dto, entity);
            var updatedEntity =  repository.save(entity);
            return mapper.toDto(updatedEntity);
        } catch (Exception e) {
            var msg = "Failed to create Person: " + dto;
            log.error("{}, reason: {}", msg, e.toString());
            throw new PersonDaoException(msg, e);
        }
    }


}

