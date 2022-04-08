package com.netflix.like.media.controllers;

import java.util.List;
import java.util.Objects;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.like.media.models.Media;
import com.netflix.like.media.repositories.MediaRepository;

@RestController
public class MediaController {

	@Autowired
	MediaRepository mediaRepo;
	
	RestTemplate restTemplate = new RestTemplate();

	//String urlUser = "http://localhost:8081/";
	String urlUser = "http://user-webservice-app:8081/";
	
	@GetMapping("/")
	public ResponseEntity<List<Media>> getAllMedia(@RequestBody int idUser) throws JsonProcessingException {
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
			return ResponseEntity.ok(mediaRepo.findAll());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
}
