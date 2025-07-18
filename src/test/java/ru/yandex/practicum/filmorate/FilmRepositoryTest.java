package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import ru.yandex.practicum.filmorate.storage.db.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.db.repository.FilmRepository;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmMapper.class})
public class FilmRepositoryTest {
    @Autowired
    private FilmRepository filmRepository;

    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2025, 7, 18));
        film.setDuration(120);
        film.setMpa(new AgeRating(1, "G"));
    }

    @Test
    void addFilm() {
        Film addFilm = filmRepository.addFilm(film);

        assertThat(addFilm.getId()).isNotNull();
        assertThat(addFilm.getName()).isEqualTo(film.getName());
        assertThat(addFilm.getDescription()).isEqualTo(film.getDescription());
        assertThat(addFilm.getReleaseDate()).isEqualTo(film.getReleaseDate());
        assertThat(addFilm.getDuration()).isEqualTo(film.getDuration());
        assertThat(addFilm.getMpa()).isEqualTo(film.getMpa());
    }

    @Test
    void updateFilm() {
        Film addFilm = filmRepository.addFilm(film);
        addFilm.setName("Updated the name");
        Film updatedFilm = filmRepository.updateFilm(addFilm);

        assertThat(updatedFilm.getName()).isEqualTo("Updated the name");
    }

    @Test
    void getListFilms() {
        filmRepository.addFilm(film);

        assertThat(filmRepository.getListFilms()).hasSize(1);
    }

    @Test
    void getFilmById() {
        Film addFilm = filmRepository.addFilm(film);

        Optional<Film> retrievedFilm = filmRepository.getFilmById(addFilm.getId());

        assertThat(retrievedFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).isEqualTo(addFilm)
                );
    }

    @Test
    void removeFilm() {
        Film addFilm = filmRepository.addFilm(film);
        filmRepository.removeFilm(addFilm.getId());

        assertThat(filmRepository.getFilmById(addFilm.getId())).isEmpty();
    }
}
