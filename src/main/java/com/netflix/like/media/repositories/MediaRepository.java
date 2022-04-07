package com.netflix.like.media.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.netflix.like.media.models.Media;

@Repository
public interface MediaRepository extends MongoRepository<Media, Integer> {

}
