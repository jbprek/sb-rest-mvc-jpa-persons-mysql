package com.foo.app.service;

import com.foo.app.db.PersonEntity;
import com.foo.app.db.PersonEntityRepository;
import com.foo.app.rest.PersonInDto;
import com.foo.app.rest.PersonOutDto;
import com.foo.app.service.exception.PersonDaoException;
import com.foo.app.service.exception.PersonDaoNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PersonDaoServiceImpl implements PersonDaoService {
    private final PersonMapper mapper;
    private final PersonEntityRepository repository;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PersonOutDto createPerson(PersonInDto dto) {
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

    @Transactional(readOnly = true)
    @Override
    public PersonOutDto getPerson(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new PersonDaoNotFoundException("Person Not found id: " + id));
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PersonOutDto> getAll() {
        return mapper.toDTOs(repository.findAll());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PersonOutDto updatePerson(Long id, PersonInDto dto) {
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
}

