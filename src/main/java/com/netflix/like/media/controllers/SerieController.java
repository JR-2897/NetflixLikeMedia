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
import com.netflix.like.media.models.Episode;
import com.netflix.like.media.models.Media.Country;
import com.netflix.like.media.models.Serie;
import com.netflix.like.media.repositories.SerieRepository;

@RestController
public class SerieController {

	@Autowired
	SerieRepository serieRepo;

	RestTemplate restTemplate = new RestTemplate();

	//String urlUser = "http://localhost:8081/";
	String urlUser = "http://user-webservice-app:8081/";

	@PostMapping("/series/{idUser}")
	public ResponseEntity<List<Serie>> getAllSerie(@PathVariable int idUser, @RequestBody Country country)
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
			List<Serie> listAllSeries = serieRepo.getSerieByCountry(country);
			List<Serie> listSerieActive = new ArrayList<>();
			for (Serie serie : listAllSeries) {
				if (serie.isActive()) {
					listSerieActive.add(serie);
				}
			}
			return ResponseEntity.ok(listSerieActive);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/serie/{idSerie}/{idUser}")
	public ResponseEntity<Serie> getSerieById(@PathVariable int idMovie, @PathVariable int idUser,
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
			Optional<Serie> resultRequest = serieRepo.findById(idMovie);
			if (resultRequest != null) {
				Serie serie = resultRequest.get();
				if (serie.isActive() && serie.getCountry().contains(country)) {
					return ResponseEntity.ok(serie);
				} else
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/add/serie/{idUser}")
	public ResponseEntity<Object> addSerieToDatabase(@PathVariable int idUser, @RequestBody Serie serieToAdd)
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
			Serie serieAdded = serieRepo.save(serieToAdd);
			if (Objects.isNull(serieAdded)) {
				return ResponseEntity.noContent().build();
			}
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(serieAdded.getId()).toUri();
			return ResponseEntity.created(location).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PutMapping("/add/episode/serie/{idSerie}/{idUser}")
	public ResponseEntity<Serie> addEpisodeToASerieToDatabase(@PathVariable int idUser, @PathVariable int idSerie,
			@RequestBody Episode episode) throws JsonProcessingException {
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
			Optional<Serie> serieForAddEpisode = serieRepo.findById(idSerie);
			int idEpisode = episode.getId();
			Serie serieUpdate = null;
			if (serieForAddEpisode != null && idEpisode != 0) {
				serieUpdate = serieForAddEpisode.get();
				List<Integer> listIdEpisode = serieUpdate.getEpisodes();
				listIdEpisode.add(idEpisode);
				serieUpdate.setEpisodes(listIdEpisode);
			}
			Serie serie = serieRepo.save(serieUpdate);
			if (!Objects.isNull(serie)) {
				return ResponseEntity.ok(serie);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/delete/serie/{idSerie}")
	public ResponseEntity<Serie> deleteSerieToDatabase(@RequestBody int idUser, @PathVariable int idSerie)
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
			Optional<Serie> resultRequest = serieRepo.findById(idSerie);
			Serie serieToDelete = null;
			if (resultRequest != null) {
				serieToDelete = resultRequest.get();
			}
			serieToDelete.setActive(false);
			Serie serie = serieRepo.save(serieToDelete);
			if (!Objects.isNull(serie)) {
				return ResponseEntity.ok(serie);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

}
