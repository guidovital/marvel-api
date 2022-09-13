package com.growin.marvelapi.controller.character;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.growin.marvelapi.exception.InvalidParameterException;
import com.growin.marvelapi.integration.CharactersClient;
import com.growin.marvelapi.integration.TranslatorClient;
import com.growin.marvelapi.model.Character;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/characters")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CharactersController {

	private CharactersClient charactersService;
	private TranslatorClient translatorService;

	@ApiOperation(value = "Get marvel characters", notes = "Provides characters ids")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of characters", response = Set.class),
			@ApiResponse(code = 500, message = "Internal server error") })
	@GetMapping
	public ResponseEntity<Set<Integer>> getCaracters() {
		Set<Integer> keySet = charactersService.getCharacters().keySet();
		return ResponseEntity.ok(keySet);
	}

	@ApiOperation(value = "Get characters by ID", notes = "Provides characters by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of character", response = Character.class),
			@ApiResponse(code = 500, message = "Internal server error") })
	@GetMapping("/{characterId}")
	public Character getCharactersById(@PathVariable("characterId") int id, @RequestParam(value = "languageCode", required = false) Locale languageCode) {
		var character = charactersService.getCharacterById(id);
		Locale[] availableLocales = Locale.getAvailableLocales();
		if(languageCode != null) {
			if(!Arrays.asList(availableLocales).contains(languageCode)) {
				throw new InvalidParameterException("This languageCode is not valid: "+ languageCode);
			}
			var descriptionTranslated = translatorService.translate(character.getDescription(), languageCode);
			character.setDescription(descriptionTranslated);
		} 
		
		return character;
	}

}
