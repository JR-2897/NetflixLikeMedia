package com.netflix.like.media.models;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("episode")
public class Episode implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private String name;
	private String synopsis;
	private float duration;
	
	public Episode(Integer id, String name, String synopsis, float duration) {
		super();
		this.id = id;
		this.name = name;
		this.synopsis = synopsis;
		this.duration = duration;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public float getDuration() {
		return duration;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	
}
