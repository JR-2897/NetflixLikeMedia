package com.netflix.like.media.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.netflix.like.media.models.Serie;
import com.netflix.like.media.models.Media.Country;

public interface SerieRepository extends MongoRepository<Serie, Integer> {
	public List<Serie> getSerieByCountry(Country codeCountry);
}
