package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

import lombok.Data;

import java.time.LocalDate;

/**
 * User
 */
@Data
public class User {

    @PositiveOrZero(message = "ID не должен быть отрицательным числом.")
    private long id;

    @NotBlank(message = "Электронная почта не долна быть пустой.")
    @Email(message = "Некорректный E-mail адрес.")
    private String email;

    @NotBlank(message = "Логин не должен быть пустым.")
    // @Pattern(regexp = "^[^\\s]+$", message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
}
