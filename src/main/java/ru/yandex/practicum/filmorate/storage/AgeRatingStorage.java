package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.AgeRating;

import java.util.List;
import java.util.Optional;

public interface AgeRatingStorage {
    List<AgeRating> getListAgeRatings();

    Optional<AgeRating> getAgeRatingById(long id);
}
