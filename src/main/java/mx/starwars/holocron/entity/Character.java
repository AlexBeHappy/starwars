package mx.starwars.holocron.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "character")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column
    String name;

    @Column
    private int height;

    @Column
    private float mass;

    @Column
    private String hairColor;

    @Column
    private String dob;

    @Lob
    @Column(name = "img", columnDefinition = "BLOB")
    private byte[] img;

    @ManyToOne
    @JoinColumn(name = "homeworld_id")
    private Planet homeworld;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @ManyToMany
    @JoinTable(
            name = "character_film",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "film_id")
    )
    private List<Film> films;
}
