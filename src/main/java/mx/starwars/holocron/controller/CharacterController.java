package mx.starwars.holocron.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.starwars.holocron.constants.AppConstants;
import mx.starwars.holocron.dto.ApiRs;
import mx.starwars.holocron.dto.CharacterDto;
import mx.starwars.holocron.dto.PaginatedResponse;
import mx.starwars.holocron.exception.ApiException;
import mx.starwars.holocron.service.CharacterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Tag(name = "Personajes", description = "Consulta y búsqueda de personajes en el universo Star Wars")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/starwars")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Operation(
            summary = "Listar personajes",
            description = "Obtiene una lista paginada de personajes"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Personajes obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = ApiRs.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron personajes")
    })
    @GetMapping("/people")
    public ResponseEntity<ApiRs<PaginatedResponse<CharacterDto>>> getCharacters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        if (page < 0 || size < 1) {
            throw new ApiException(AppConstants.INVALID_PAGINATION, HttpStatus.BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CharacterDto> pageResult = characterService.getCharacters(pageable);

        String message = pageResult.isEmpty()
                ? AppConstants.CHARACTER_NOT_FOUND
                : AppConstants.CHARACTER_FOUND;

        PaginatedResponse<CharacterDto> data = new PaginatedResponse<>(pageResult, message);
        return ResponseEntity.ok(new ApiRs<>(message, data));
    }

    @Operation(
            summary = "Buscar personajes",
            description = "Filtra personajes por nombre, planeta natal, género o color de cabello"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultados de búsqueda",
                    content = @Content(schema = @Schema(implementation = ApiRs.class))),
            @ApiResponse(responseCode = "400", description = "Filtros no proporcionados correctamente"),
            @ApiResponse(responseCode = "204", description = "No se encontraron coincidencias")
    })
    @GetMapping("/search")
    public ResponseEntity<ApiRs<List<CharacterDto>>> searchCharacters(
            @RequestParam(name = "Name", required = false) String name,
            @RequestParam(name = "Homeworld", required = false) String homeworld,
            @RequestParam(name = "Gender", required = false) String gender,
            @RequestParam(name = "HairColor", required = false) String hairColor
    ) {
        if (Stream.of(name, homeworld, gender, hairColor).allMatch(Objects::isNull)) {
            throw new ApiException(AppConstants.CHARACTER_SEARCH_MISSING_FILTERS, HttpStatus.BAD_REQUEST);
        }
        List<CharacterDto> result = characterService.searchCharacters(name, homeworld, gender, hairColor);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new ApiRs<>(AppConstants.CHARACTER_FOUND, result));
    }
}
