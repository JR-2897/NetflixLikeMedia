package com.netflix.like.media.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.netflix.like.media.models.Episode;

@Repository
public interface EpisodesRepository extends MongoRepository<Episode, Integer> {
	
	public Episode findByName(String name);

}
