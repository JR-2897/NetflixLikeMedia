package com.netflix.like.media.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.like.media.models.Movie;
import com.netflix.like.media.models.Media.Country;
import com.netflix.like.media.repositories.MovieRepository;

@RestController
public class MovieController {

	@Autowired
	MovieRepository movieRepo;

	RestTemplate restTemplate = new RestTemplate();

	String urlUser = "http://localhost:8081/";

	@GetMapping("/movies/{idUser}")
	public ResponseEntity<List<Movie>> getAllMovie(@PathVariable int idUser, @RequestBody Country country)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		Object obj = restTemplate.exchange(urlUser + "user/status/" + idUser, HttpMethod.GET, request, Object.class)
				.getBody();
		if (Objects.isNull(obj)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String jsonStr = mapper.writeValueAsString(obj);
		JSONObject json = new JSONObject(jsonStr);
		if (!json.getString("status").equals("SUSPENDED") || !json.getString("status").equals("REMOVED")) {
			List<Movie> listAllMovies = movieRepo.getMovieByCountry(country);
			List<Movie> listMovieActive = new ArrayList<>();
			for (Movie movie : listAllMovies) {
				if (movie.isActive() && movie.getCountry().contains(country)) {
					listMovieActive.add(movie);
				}
			}
			return ResponseEntity.ok(listMovieActive);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/movie/{idMovie}/{idUser}")
	public ResponseEntity<Movie> getMovieById(@PathVariable int idMovie, @PathVariable int idUser,
			@RequestBody Country country) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		Object obj = restTemplate.exchange(urlUser + "user/status/" + idUser, HttpMethod.GET, request, Object.class)
				.getBody();
		if (Objects.isNull(obj)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String jsonStr = mapper.writeValueAsString(obj);
		JSONObject json = new JSONObject(jsonStr);
		if (!json.getString("status").equals("SUSPENDED") || !json.getString("status").equals("REMOVED")) {
			Optional<Movie> resultRequest = movieRepo.findById(idMovie);
			if (resultRequest != null) {
				Movie movie = resultRequest.get();
				if (movie.isActive() && movie.getCountry().contains(country)) {
					return ResponseEntity.ok(movie);
				} else
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/add/movie/{idUser}")
	public ResponseEntity<Object> addMovieToDatabase(@PathVariable int idUser, @RequestBody Movie movieToAdd)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		Object obj = restTemplate.exchange(urlUser + "user/status/" + idUser, HttpMethod.GET, request, Object.class)
				.getBody();
		if (Objects.isNull(obj)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String jsonStr = mapper.writeValueAsString(obj);
		JSONObject json = new JSONObject(jsonStr);
		if (json.getString("status").equals("ADMIN")) {
			Movie movieAdded = movieRepo.save(movieToAdd);
			if (Objects.isNull(movieAdded)) {
				return ResponseEntity.noContent().build();
			}
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(movieAdded.getId()).toUri();
			return ResponseEntity.created(location).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PutMapping("/update/movie/{idMovie}/{idUser}")
	public ResponseEntity<Movie> updateMovieToDatabase(@PathVariable int idUser, @PathVariable int idMovie,
			@RequestBody Movie updatedMovie) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		Object obj = restTemplate.exchange(urlUser + "user/status/" + idUser, HttpMethod.GET, request, Object.class)
				.getBody();
		if (Objects.isNull(obj)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String jsonStr = mapper.writeValueAsString(obj);
		JSONObject json = new JSONObject(jsonStr);
		if (json.getString("status").equals("ADMIN")) {
			Movie movieUpdate = movieRepo.save(updatedMovie);
			if (!Objects.isNull(movieUpdate)) {
				return ResponseEntity.ok(movieUpdate);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/delete/movie/{idMovie}")
	public ResponseEntity<Movie> deleteMovieToDatabase(@PathVariable int idMovie, @RequestBody int idUser)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		Object obj = restTemplate.exchange(urlUser + "user/status/" + idUser, HttpMethod.GET, request, Object.class)
				.getBody();
		if (Objects.isNull(obj)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String jsonStr = mapper.writeValueAsString(obj);
		JSONObject json = new JSONObject(jsonStr);
		if (json.getString("status").equals("ADMIN")) {
			Optional<Movie> resultRequest = movieRepo.findById(idMovie);
			Movie movieToDelete = null;
			if (resultRequest != null) {
				movieToDelete = resultRequest.get();
			}
			movieToDelete.setActive(false);
			Movie movie = movieRepo.save(movieToDelete);
			if (!Objects.isNull(movie)) {
				return ResponseEntity.ok(movie);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

}
