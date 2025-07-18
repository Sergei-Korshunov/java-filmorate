package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.service.AgeRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class AgeRatingController {
    private final AgeRatingService ageRatingService;

    @Autowired
    public AgeRatingController(AgeRatingService ageRatingService) {
        this.ageRatingService = ageRatingService;
    }

    @GetMapping
    public List<AgeRating> getListAgeRatings() {
        return ageRatingService.getListAgeRatings();
    }

    @GetMapping("/{id}")
    public AgeRating getAgeRatingById(@PathVariable long id) {
        return ageRatingService.getAgeRatingById(id);
    }
}
