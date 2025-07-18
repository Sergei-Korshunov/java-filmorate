package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UserController.class, FilmController.class})
public class DataValidationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @MockBean
    private UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    Film correctFilm;
    Film unCorrectFilm;

    User correctUser;
    User unCorrectUser;

    @BeforeEach
    void setUp() {
        correctFilm = createCorrectFilm();
        unCorrectFilm = createUnCorrectFilm();

        correctUser = createCorrectUser();
        unCorrectUser = createUnCorrectUser();
    }

    @Test
    void postCorrectFilm() throws Exception {
        when(filmService.addFilm(correctFilm)).thenReturn(correctFilm);

        ResultActions resultActions = mockMvc.perform(post("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(correctFilm)));

        resultActions.andExpectAll(
                status().isOk()
        );
    }

    @Test
    void postUnCorrectFilm() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unCorrectFilm)));

        resultActions.andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void updateNonExistentFilm() throws Exception {
        Film updateNonExistentFilm = new Film();
        updateNonExistentFilm.setId(1000);
        updateNonExistentFilm.setName("Film 1");
        updateNonExistentFilm.setDescription("New description 1");
        updateNonExistentFilm.setDuration(120);
        updateNonExistentFilm.setReleaseDate(LocalDate.now());
        updateNonExistentFilm.setMpa(new AgeRating(1, "G"));

        when(filmService.updateFilm(updateNonExistentFilm)).thenThrow(
                new NotFoundException(String.format("Фильм с id - %s не найден", updateNonExistentFilm.getId())));

        ResultActions resultActions = mockMvc.perform(put("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateNonExistentFilm)));

        resultActions.andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    void getFilm() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/films"));

        resultActions.andExpectAll(
                status().isOk()
        );
    }

    private Film createCorrectFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film 1");
        film.setDescription("Description 1");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.now());
        film.setMpa(new AgeRating(1, "G"));

        return film;
    }

    private Film createUnCorrectFilm() {
        Film film = new Film();
        film.setId(2);
        film.setName("Film 2");
        film.setDescription("Description 2");
        film.setDuration(-120);
        film.setReleaseDate(LocalDate.of(1700, 1, 1));

        return film;
    }

    @Test
    void postCorrectUser() throws Exception {
        when(userService.addUser(correctUser)).thenReturn(correctUser);

        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(correctUser))
        );

        resultActions.andExpectAll(
                status().isOk()
        );
    }

    @Test
    void postUnCorrectUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unCorrectUser))
        );

        resultActions.andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void updateNonExistentUser() throws Exception {
        User user = new User();
        user.setId(1000);
        user.setLogin("Login1");
        user.setEmail("login1@mail.nety");
        user.setBirthday(LocalDate.now());

        when(userService.updateUser(user)).thenThrow(
                new NotFoundException(String.format("Пользователь с id - %s не найден", user.getId())));

        ResultActions resultActions = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
        );

        resultActions.andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    void getUsers() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/users"));

        resultActions.andExpectAll(
                status().isOk()
        );
    }

    private User createCorrectUser() {
        User user = new User();
        user.setId(1);
        user.setLogin("Login1");
        user.setEmail("login1@mail.net");
        user.setBirthday(LocalDate.now());

        return user;
    }

    private User createUnCorrectUser() {
        User user = new User();
        user.setId(2);
        user.setLogin("Login 2");
        user.setEmail("login2mail.net");
        user.setBirthday(LocalDate.now().plusYears(5));

        return user;
    }
}
