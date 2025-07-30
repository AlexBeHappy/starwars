package mx.starwars.holocron.repository;

import mx.starwars.holocron.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {


    /**
     * Busca personajes que coincidan con los filtros proporcionados.
     * Todos los parámetros son opcionales: si alguno es null no se usa como filtro.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     *
     * @param name      nombre
     * @param homeworld planeta
     * @param gender    género
     * @param hairColor color de cabello
     * @return lista de personajes que cumplen con los criterios de búsqueda
     */
    @Query("""
                SELECT c FROM character c
                WHERE (:name IS NULL OR LOWER(c.name) = LOWER(:name))
                  AND (:homeworld IS NULL OR LOWER(c.homeworld.name) = LOWER(:homeworld))
                  AND (:gender IS NULL OR LOWER(c.gender.label) = LOWER(:gender))
                  AND (:hairColor IS NULL OR LOWER(c.hairColor) = LOWER(:hairColor))
            """)
    List<Character> searchByFilters(
            @Param("name") String name,
            @Param("homeworld") String homeworld,
            @Param("gender") String gender,
            @Param("hairColor") String hairColor
    );
}

