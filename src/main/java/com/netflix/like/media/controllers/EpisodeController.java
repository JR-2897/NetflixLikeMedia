package com.netflix.like.media.controllers;

import java.net.URI;
import java.util.Objects;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.netflix.like.media.repositories.EpisodesRepository;

@RestController
public class EpisodeController {

	@Autowired
	EpisodesRepository episodesRepo;

	RestTemplate restTemplate = new RestTemplate();

	//String urlUser = "http://localhost:8081/";
	String urlUser = "http://user-webservice-app:8081/";
	
	@PostMapping("/episodes/{nameEpisode}")
	public ResponseEntity<Episode> getEpisodeByName(@PathVariable String nameEpisode, @RequestBody int idUser)
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
		if (json.getString("status").equals("ACTIVE") || json.getString("status").equals("ADMIN")) {
			return ResponseEntity.ok(episodesRepo.findByName(nameEpisode));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/add/episode/{idUser}")
	public ResponseEntity<Object> addEpisodeToDatabase(@PathVariable int idUser, @RequestBody Episode episodeToAdd)
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
		if (json.getString("status").equals("ACTIVE") || json.getString("status").equals("ADMIN")) {
			Episode episodeAdded = episodesRepo.save(episodeToAdd);
			if (Objects.isNull(episodeAdded)) {
				return ResponseEntity.noContent().build();
			}
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(episodeAdded.getId()).toUri();
			return ResponseEntity.created(location).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PutMapping("/add/episode/{idEpisode}/{idUser}")
	public ResponseEntity<Episode> updateEpisodeToDatabase(@PathVariable int idUser, @PathVariable int idEpisode,
			@RequestBody Episode updateEpisode) throws JsonProcessingException {
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
		if (json.getString("status").equals("ACTIVE") || json.getString("status").equals("ADMIN")) {
			Episode episodeToUpdate = episodesRepo.save(updateEpisode);
			if (Objects.isNull(episodeToUpdate)) {
				return ResponseEntity.noContent().build();
			}
			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
}