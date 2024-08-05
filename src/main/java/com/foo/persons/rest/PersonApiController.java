package com.foo.persons.rest;

import com.foo.persons.service.PersonDaoService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/persons")
public class PersonApiController {

    private final PersonDaoService daoService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PersonDto create(@RequestBody @Validated(ValidateOnCreate.class) PersonDto dto) {
       return daoService.createPerson(dto);
    }

    @GetMapping("/{id}")
    public PersonDto read(@PathVariable @Min(1) final Long id) {
       return daoService.getPerson(id);
    }

    @GetMapping(path = "/all")
    public List<PersonDto> readAll() {
        return daoService.getAll();
    }

    @PutMapping
    public PersonDto update(@RequestBody @Validated(ValidateOnUpdate.class) PersonDto dto) {
        return daoService.updatePerson(dto);
    }

    @PatchMapping(path = "/{id}")
    public PersonDto patch(@PathVariable @Min(1) Long id,
                           @RequestBody @Validated PersonDto dto)
    {
        return daoService.patchPerson(id, dto);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable @Min(1) Long id) {
        daoService.deletePerson(id);
    }


}