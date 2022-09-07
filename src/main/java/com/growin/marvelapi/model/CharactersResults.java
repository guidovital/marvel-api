package com.growin.marvelapi.model;

import java.util.List;

import lombok.Data;

@Data
public class CharactersResults {

	private List<Character> results;
	
	private Integer count;

}
