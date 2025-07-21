package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private long id;

    @NotBlank(message = "Электронная почта не должна быть пустой")
    @Email(message = "Некорректный E-mail адрес")
    private String email;

    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "^[^\\s]+$", message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    @NotNull(message = "Дата рождения должна быть указана")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}
