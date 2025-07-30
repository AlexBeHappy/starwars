package mx.starwars.holocron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.starwars.holocron.entity.Character;
import mx.starwars.holocron.entity.Film;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDto {
    private String name;
    private int height;
    private float mass;
    private String hairColor;
    private String gender;
    private String homeworld;
    private String dob;
    private List<String> films;
   private String imgBase64;

    public CharacterDto(Character character) {
        this.name = character.getName();
        this.height = character.getHeight();
        this.mass = character.getMass();
        this.hairColor = character.getHairColor();
        this.gender = character.getGender().getLabel();
        this.homeworld = character.getHomeworld().getName();
        this.dob = character.getDob();
        this.films = character.getFilms()
                .stream()
                .map(Film::getTitle)
                .collect(Collectors.toList());
       this.imgBase64 = character.getImg() != null ?
                Base64.getEncoder().encodeToString(character.getImg()) : null;
    }
}
