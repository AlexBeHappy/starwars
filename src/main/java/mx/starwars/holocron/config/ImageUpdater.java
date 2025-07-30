package mx.starwars.holocron.config;

import mx.starwars.holocron.entity.Character;
import mx.starwars.holocron.repository.CharacterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Clase que actualiza las imágenes de los personajes en la base de datos al iniciar la aplicación.
 */
@Component
public class ImageUpdater implements CommandLineRunner {

    private final CharacterRepository characterRepository;

    public ImageUpdater(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    private static final String IMAGE_FOLDER = "src/main/resources/img/";
    private static final String IMAGE_EXTENSION = ".jpg";

    /**
     * Método que se ejecuta al iniciar la aplicación.
     * Recorre todos los personajes, busca su imagen correspondiente en el sistema de archivos
     *
     * @param args argumentos de línea de comandos pasados al iniciar la aplicación.
     * @throws IOException si ocurre un error al leer la imagen desde el sistema de archivos.
     */
    @Override
    public void run(String[] args) throws IOException {
        List<Character> characters = characterRepository.findAll();
        for (Character character : characters) {
            Path imagePath = Paths.get(IMAGE_FOLDER + character.getId() + IMAGE_EXTENSION);
            if (Files.exists(imagePath)) {
                byte[] imageBytes = Files.readAllBytes(imagePath);
                character.setImg(imageBytes);
                characterRepository.save(character);
            } else {
                System.out.println("Imagen no encontrada para el personaje ID: " + character.getId());
            }
        }
    }
}
