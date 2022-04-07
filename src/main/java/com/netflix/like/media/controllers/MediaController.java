package com.netflix.like.media.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.like.media.models.Media;
import com.netflix.like.media.repositories.MediaRepository;

@RestController
public class MediaController {

	@Autowired
	MediaRepository mediaRepo;
	
	@GetMapping
	public List<Media> getAllMedia() {
		return mediaRepo.findAll();
	}
	
}
