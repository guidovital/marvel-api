package com.growin.marvelapi.integration;

import java.util.Map;

import com.growin.marvelapi.model.Character;

public interface CharactersClient {
	
	public Map<Integer, Character> getCharacters();
	public Character getCharacterById(int id);
}
