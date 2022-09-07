package com.growin.marvelapi.model;

import lombok.Data;

@Data
public class Character {
	
	private Integer id;
	private String name;
	private String description;
	private Thumbnail thumbnail;
}