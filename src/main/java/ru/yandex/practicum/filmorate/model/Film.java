package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.DateValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private long id;

    @NotBlank(message = "Название не должно быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания не должна превышать двухсот символов")
    private String description;

    @DateValidation
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма не может быть отрицательным числом")
    private int duration;

    private Set<Long> likes = new HashSet<>();

    private Set<Genre> genres = new HashSet<>();

    @NotNull(message = "Возрастной рейтинг не должен быть пустым")
    private AgeRating mpa;
}
