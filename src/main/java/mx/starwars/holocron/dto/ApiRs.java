package mx.starwars.holocron.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class ApiRs<T> {
    private String message;
    private T data;

    public ApiRs(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
