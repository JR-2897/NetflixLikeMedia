package com.netflix.like.media.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("media")
public class Media implements Serializable {

	public enum Country {
		FR, US, DE, GB;
	}
	
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private String name;
	private String description;
	private List<Country> country;
	private boolean active;
	
	public Media(Integer id, String name, String description, List<Country> country, boolean active) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.country = country;
		this.active = active;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Country> getCountry() {
		return country;
	}

	public void setCountry(List<Country> country) {
		this.country = country;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
