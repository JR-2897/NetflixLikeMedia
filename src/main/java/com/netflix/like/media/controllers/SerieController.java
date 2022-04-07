package com.netflix.like.media.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import com.netflix.like.media.models.Episode;
import com.netflix.like.media.models.Media;
import com.netflix.like.media.models.Serie;
import com.netflix.like.media.repositories.SerieRepository;

@RestController
public class SerieController {

	@Autowired
	SerieRepository serieRepo;
	
	@GetMapping("/movies")
	public List<Serie> getAllMovie(){
		return serieRepo.getSerieByCountry(Media.Country.FR);
	}
	
	@PostMapping("/add/serie")
	public ResponseEntity<Object> addSerieToDatabase(@RequestBody Serie serieToAdd) {
		Serie serieAdded = serieRepo.save(serieToAdd);
		if(Objects.isNull(serieAdded)) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(serieAdded.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/add/episode/serie/{idSerie}")
	public Serie addEpisodeToASerieToDatabase(@PathVariable int idSerie, @RequestBody Episode episode) {
		Optional<Serie> serieForAddEpisode = serieRepo.findById(idSerie);
		int idEpisode = episode.getId();
		Serie serieUpdate = null;
		if(serieForAddEpisode != null && idEpisode != 0)
		{
			serieUpdate = serieForAddEpisode.get();
			List<Integer> listIdEpisode = serieUpdate.getEpisodes();
			listIdEpisode.add(idEpisode);
			serieUpdate.setEpisodes(listIdEpisode);
		}
		Serie serie = serieRepo.save(serieUpdate);
		if(!Objects.isNull(serie)) {
			return serie;
		}
		return null;
	}
	
	@DeleteMapping("/delete/serie/{id}")
	public Serie deleteSerieToDatabase(@PathVariable int id) {
		Optional<Serie> resultRequest = serieRepo.findById(id);
		Serie serieToDelete = null;
		if(resultRequest != null)
		{
			serieToDelete = resultRequest.get();
		}
		serieToDelete.setActive(false);
		Serie serie = serieRepo.save(serieToDelete);
		if(!Objects.isNull(serie))
		{
			return serie;
		}
		return null;
	}

}
