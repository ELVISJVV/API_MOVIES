package com.projects.movies.controllers;

import com.projects.movies.models.Movie;
import com.projects.movies.repositores.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @CrossOrigin
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }


    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieRepository.findById(id);

        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @CrossOrigin
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie newMovie = movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMovie);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        movieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        movie.setId(id);
        movieRepository.save(movie);
        //return ResponseEntity.noContent().build();
        return ResponseEntity.ok(movie);
    }


    @CrossOrigin
    @GetMapping("/vote/{id}/{raiting}")
    public ResponseEntity<Movie> voteMovie(@PathVariable Long id, @PathVariable double raiting) {
        if (!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Movie movie = movieRepository.findById(id).get();

        double newRaiting = ((movie.getRating() * movie.getVotes())+raiting) / (movie.getVotes() + 1);

        movie.setRating(newRaiting);
        movie.setVotes(movie.getVotes() + 1);

        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(savedMovie);
    }
}


