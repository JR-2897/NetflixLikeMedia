package com.netflix.like.media.controllers;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.netflix.like.media.models.Episode;
import com.netflix.like.media.repositories.EpisodesRepository;

@RestController
public class EpisodeController {

	@Autowired
	EpisodesRepository episodesRepo;

	@GetMapping("/episodes/{nameEpisode}")
	public Episode getEpisodeByName(@PathVariable String nameEpisode) {
		return episodesRepo.findByName(nameEpisode);
	}
	
	@PostMapping("/add/episode")
	public ResponseEntity<Object> addEpisodeToDatabase(@RequestBody Episode episodeToAdd) {
		Episode episodeAdded = episodesRepo.save(episodeToAdd);
		if(Objects.isNull(episodeAdded)) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(episodeAdded.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}