package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.db.repository.AgeRatingRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.AgeRating;

import java.util.List;

@Service
public class AgeRatingService {
    private final AgeRatingRepository ageRatingRepository;

    @Autowired
    public AgeRatingService(AgeRatingRepository ageRatingRepository) {
        this.ageRatingRepository = ageRatingRepository;
    }

    public List<AgeRating> getListAgeRatings() {
        return ageRatingRepository.getListAgeRatings();
    }

    public AgeRating getAgeRatingById(long id) {
        return ageRatingRepository.getAgeRatingById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг с id - %s не найден.", id)));
    }
}
