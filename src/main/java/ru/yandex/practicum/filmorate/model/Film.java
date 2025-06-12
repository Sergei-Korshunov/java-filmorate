package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.Data;

import ru.yandex.practicum.filmorate.annotation.DateValidation;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    @PositiveOrZero(message = "ID не должен быть отрицательным числом.")
    private long id;

    @NotBlank(message = "Название не должно быть пустым.")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания не должна превышать двухста символов.")
    private String description;

    @DateValidation
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма не может быть отрицательным числом.")
    private int duration;

}
