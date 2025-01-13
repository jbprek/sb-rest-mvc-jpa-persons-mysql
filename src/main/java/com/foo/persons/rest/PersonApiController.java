package com.foo.persons.rest;

import com.foo.persons.service.PersonDaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Person API", description = "Operations related to persons")
public class PersonApiController {

    private final PersonDaoService daoService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(
            summary = "Create a new person",
            description = "Add a new person by providing their details in the request body",
            tags = {"Person API"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the new person",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Person created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonDto.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public PersonDto create(@RequestBody @Validated(ValidateOnCreate.class) PersonDto dto) {
        return daoService.createPerson(dto);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Person by ID",
            description = "Provide an ID to look up specific person details",
            tags = {"Person API"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Unique identifier of the person to retrieve",
                            required = true,
                            example = "123",
                            schema = @Schema(type = "integer", format = "int64", minimum = "1")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Person found and details retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Person not found for the given ID",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    public PersonDto read(@PathVariable @Min(1) final Long id) {
        return daoService.getPerson(id);
    }

    @GetMapping(path = "/all")
    @Operation(
            summary = "List all persons",
            description = "Retrieve a list of all persons available in the database",
            tags = {"Person API"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of persons retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class)
                            )),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public List<PersonDto> readAll() {
        return daoService.getAll();
    }

    @PutMapping
    @Operation(
            summary = "Update an existing person",
            description = "Modify an existing person by providing their ID and updated details",
            tags = {"Person API"},

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated details of the person",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Person updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonDto.class)
                            )),
                    @ApiResponse(responseCode = "404", description = "Person not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public PersonDto update(@RequestBody @Validated(ValidateOnUpdate.class) PersonDto dto) {
        return daoService.updatePerson(dto);
    }

    @PatchMapping(path = "/{id}")
    @Operation(
            summary = "Partial Update of an existing person",
            description = "Modify an existing person by providing their ID and updated details",
            tags = {"Person API"},
            parameters = {
                    @Parameter(name = "id", description = "ID of the person to update", required = true, example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated details of the person",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Person updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonDto.class)
                            )),
                    @ApiResponse(responseCode = "404", description = "Person not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public PersonDto patch(@PathVariable @Min(1) Long id,
                           @RequestBody @Validated PersonDto dto) {
        return daoService.patchPerson(id, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    @Operation(
            summary = "Delete a person",
            description = "Remove a person from the database by providing their ID",
            parameters = {
                    @Parameter(name = "id", description = "ID of the person to delete", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Person not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public void delete(@PathVariable @Min(1) Long id) {
        daoService.deletePerson(id);
    }


}