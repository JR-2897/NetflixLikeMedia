package com.netflix.like.media.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.netflix.like.media.models.Media;
import com.netflix.like.media.models.Movie;
import com.netflix.like.media.repositories.MovieRepository;

@RestController
public class MovieController {

	@Autowired
	MovieRepository movieRepo;
	
	@GetMapping("/movies")
	public List<Movie> getAllMovie() {
		List<Movie> listAllMovies = movieRepo.getMovieByCountry(Media.Country.FR);
		List<Movie> listMovieActive = new ArrayList<>();
		for(Movie movie : listAllMovies)
		{
			if(movie.isActive())
			{
				listMovieActive.add(movie);
			}
		}
		return listMovieActive;
	}
	
	@GetMapping("/movie/{id}")
	public Movie getMovieById(@PathVariable int id) {
		Optional<Movie> resultRequest = movieRepo.findById(id);
		if(resultRequest != null) {
			Movie movie = resultRequest.get();
			if(movie.isActive())
			{
				return movie;
			}
			else
				return null;
		}
		return null;
	}
	
	
	@PostMapping("/add/movie")
	public ResponseEntity<Object> addMovieToDatabase(@RequestBody Movie movieToAdd) {
		Movie movieAdded = movieRepo.save(movieToAdd);
		if(Objects.isNull(movieAdded)) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(movieAdded.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/update/movie/{id}")
	public Movie updateMovieToDatabase(@PathVariable int id, @RequestBody Movie updatedMovie) {
		Movie movieUpdate = movieRepo.save(updatedMovie);
		if(!Objects.isNull(movieUpdate)) {
			return movieUpdate;
		}
		return null;
	}
	
	@DeleteMapping("/delete/movie/{id}")
	public Movie deleteMovieToDatabase(@PathVariable int id) {
		Optional<Movie> resultRequest = movieRepo.findById(id);
		Movie movieToDelete = null;
		if(resultRequest != null)
		{
			movieToDelete = resultRequest.get();
		}
		movieToDelete.setActive(false);
		Movie movie = movieRepo.save(movieToDelete);
		if(!Objects.isNull(movie))
		{
			return movie;
		}
		return null;
	}

}
