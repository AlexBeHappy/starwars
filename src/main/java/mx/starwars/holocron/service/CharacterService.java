package mx.starwars.holocron.service;

import mx.starwars.holocron.constants.AppConstants;
import mx.starwars.holocron.exception.ApiException;
import mx.starwars.holocron.repository.CharacterRepository;
import mx.starwars.holocron.dto.CharacterDto;
import mx.starwars.holocron.entity.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio relacionada con los personajes
 */
@Service
public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository repository) {
        this.characterRepository = repository;
    }

    /**
     * Obtiene una lista paginada de personajes.
     *
     * @param pageable objeto que contiene la información de paginación (página y tamaño)
     * @return una página con DTOs de personajes
     * @throws ApiException si la paginación es inválida o si ocurre un error al consultar los datos
     */
    public Page<CharacterDto> getCharacters(Pageable pageable) {
        try {
            Page<Character> page = characterRepository.findAll(pageable);
            return page.map(CharacterDto::new);
        } catch (Exception e) {
            throw new ApiException(AppConstants.CHARACTER_FETCH_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca personajes que coincidan con los filtros proporcionados.
     *
     * @param name      nombre
     * @param homeworld planeta
     * @param gender    género
     * @param hairColor color de cabello
     * @return lista de DTOs de personajes
     * @throws ApiException si ocurre un error al realizar la búsqueda
     */
    public List<CharacterDto> searchCharacters(String name, String homeworld, String gender, String hairColor) {
        try {
            return characterRepository.searchByFilters(name, homeworld, gender, hairColor)
                    .stream()
                    .map(CharacterDto::new)
                    .toList();
        } catch (Exception e) {
            throw new ApiException(AppConstants.CHARACTER_SEARCH_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
