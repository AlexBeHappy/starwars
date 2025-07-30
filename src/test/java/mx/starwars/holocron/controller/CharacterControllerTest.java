package mx.starwars.holocron.controller;

import mx.starwars.holocron.constants.AppConstants;
import mx.starwars.holocron.dto.ApiRs;
import mx.starwars.holocron.dto.CharacterDto;
import mx.starwars.holocron.dto.PaginatedResponse;
import mx.starwars.holocron.exception.ApiException;
import mx.starwars.holocron.service.CharacterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterControllerTest {

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterController characterController;

    @Test
    void listarPersonajes() {
        CharacterDto personaje = new CharacterDto();
        Page<CharacterDto> pagina = new PageImpl<>(List.of(personaje));
        when(characterService.getCharacters(PageRequest.of(0, 5))).thenReturn(pagina);
        ResponseEntity<ApiRs<PaginatedResponse<CharacterDto>>> respuesta =
                characterController.getCharacters(0, 5);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(AppConstants.CHARACTER_FOUND, respuesta.getBody().getMessage());
    }

    @Test
    void listarPersonajesSinResultados() {
        Page<CharacterDto> paginaVacia = new PageImpl<>(Collections.emptyList());
        when(characterService.getCharacters(PageRequest.of(0, 5))).thenReturn(paginaVacia);
        ResponseEntity<ApiRs<PaginatedResponse<CharacterDto>>> respuesta =
                characterController.getCharacters(0, 5);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(AppConstants.CHARACTER_NOT_FOUND, respuesta.getBody().getMessage());
    }

    @Test
    void buscarPersonajesConFiltrosCorrectos() {
        CharacterDto personaje = new CharacterDto();
        List<CharacterDto> resultado = List.of(personaje);
        when(characterService.searchCharacters("Luke", "Tatooine", "Male", "Blond"))
                .thenReturn(resultado);
        ResponseEntity<ApiRs<List<CharacterDto>>> respuesta = characterController.searchCharacters(
                "Luke", "Tatooine", "Male", "Blond"
        );
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(AppConstants.CHARACTER_FOUND, respuesta.getBody().getMessage());
        assertEquals(1, respuesta.getBody().getData().size());
    }

}
