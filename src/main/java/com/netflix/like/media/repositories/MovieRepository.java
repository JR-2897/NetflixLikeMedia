package com.netflix.like.media.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.netflix.like.media.models.Media.Country;
import com.netflix.like.media.models.Movie;

@Repository
public interface MovieRepository extends MongoRepository<Movie, Integer> {
	public List<Movie> getMovieByCountry(Country codeCountry);
}
