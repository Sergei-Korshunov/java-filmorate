package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.db.repository.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getListGenres() {
        return genreRepository.getListGenres();
    }

    public Genre getGenreById(long id) {
        return genreRepository.getGenreById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр с id - %s не найден.", id)));
    }

    public Set<Genre> getGenresForFilm(Film film) {
        return genreRepository.getGenresForFilm(film);
    }

    public Map<Long, Set<Genre>> getGenresForAllFilms() {
        return genreRepository.getGenresForAllFilms();
    }
}
