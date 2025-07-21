package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс описывает возрастной рейтинг по классификации Motion Picture Association.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgeRating {
    private long id;

    @NotNull(message = "Возрастной рейтинг должен быть указан")
    private String name;
}
