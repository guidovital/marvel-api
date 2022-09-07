package com.growin.marvelapi.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.growin.marvelapi.integration.CharactersClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Guilherme Vital
 *
 */
@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppRunner implements CommandLineRunner {

	private CharactersClient charactersService;

	@Override
	public void run(String... args) throws Exception {
		log.info("Loading characters from marvel api and caching");
		charactersService.getCharacters();
	}
}
