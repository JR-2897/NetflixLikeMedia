package com.netflix.like.media.models;

import java.util.List;

public class Movie extends Media {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int duration;

	public Movie(Integer id, String name, String description, List<Country> country, boolean active) {
		super(id, name, description, country, active);
	}
	
	public Movie(Integer id, String name, String description, List<Country> country, boolean active, int duration) {
		super(id, name, description, country, active);
		this.setDuration(duration);
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
