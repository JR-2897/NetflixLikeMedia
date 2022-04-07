package com.netflix.like.media.models;

import java.util.List;

public class Serie extends Media {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int nbEpisode;
	private List<Integer> episodes;
	
	public Serie(Integer id, String name, String description, List<Country> country, boolean active) {
		super(id, name, description, country, active);
	}
	
	public Serie(Integer id, String name, String description, List<Country> country, boolean active, List<Integer> episodes) {
		super(id, name, description, country, active);
		this.episodes = episodes;
		this.nbEpisode = episodes.size();
	}

	public int getNbEpisode() {
		return nbEpisode;
	}

	public void setNbEpisode(int nbEpisode) {
		this.nbEpisode = nbEpisode;
	}

	public List<Integer> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<Integer> episodes) {
		this.episodes = episodes;
	}
	
}
