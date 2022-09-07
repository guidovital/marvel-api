package com.growin.marvelapi.integration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.growin.marvelapi.exception.ObjectNotFoundException;
import com.growin.marvelapi.exception.ServiceUnavailable;
import com.growin.marvelapi.model.Character;
import com.growin.marvelapi.model.CharactersData;
import com.growin.marvelapi.util.CharactersUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CharactersClientImpl implements CharactersClient {

	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${marvelApiUrl}")
	public String marvelApiUrl;
	@Value("${marvelApiPublicKey}")
	public String publicKey;
	@Value("${marvelApiPrivateKey}")
	public String privateKey;

	@Cacheable(value = "marvel-api-characters")
	@Override
	public Map<Integer, Character> getCharacters() {
		log.info("Getting characters...");
		Map<Integer, Character> characterMap = new HashMap<>();
		var offset = 0;
		UriComponents uri = buildUri(offset);
		try {
			var body = restTemplate.exchange(uri.toString(), HttpMethod.GET, buildHeader(), CharactersData.class).getBody();
			addCharactersToMap(characterMap, body);
			var count = body.getData().getCount();
			while(count == 100) {
				log.info("Ainda tem personagens...");
				offset=offset+100;
				uri = buildUri(offset);
				body = restTemplate.exchange(uri.toString(), HttpMethod.GET, buildHeader(), CharactersData.class).getBody();
				addCharactersToMap(characterMap, body);
				count = body.getData().getCount();
			}
			log.info("There are {} characters", characterMap.size());
			return characterMap;
		} catch (Exception e) {
			throw new ServiceUnavailable("MarvelAPI is unavailable");
		}
	}

	@Override
	public Character getCharacterById(int id) {
		log.info("Getting character with id {}", id);
		UriComponents uri = UriComponentsBuilder.newInstance()
												.scheme("https").host(marvelApiUrl).path("/characters/" + id)
												.queryParam("ts",  CharactersUtil.timeStamp)
												.queryParam("apikey",publicKey)
												.queryParam("hash", CharactersUtil.md5hash(publicKey, privateKey,CharactersUtil.timeStamp))
												.build();
		try {
			var body = restTemplate.exchange(uri.toString(), HttpMethod.GET, buildHeader(), CharactersData.class).getBody();
			return body.getData().getResults().get(0);
		} catch (HttpClientErrorException e) {
			if(e.getRawStatusCode() == 404) {
				throw new ObjectNotFoundException("The character was not found");
			}
			return null;
		}
	}
	
	private void addCharactersToMap(Map<Integer, Character> characterMap, CharactersData body) {
		body.getData().getResults().forEach(character -> characterMap.put(character.getId(), character));
	}

	private UriComponents buildUri(int offset) {
		return UriComponentsBuilder.newInstance()
									.scheme("https").host(marvelApiUrl).path("/characters")
									.queryParam("ts",  CharactersUtil.timeStamp)
									.queryParam("apikey",publicKey)
									.queryParam("hash", CharactersUtil.md5hash(publicKey, privateKey,CharactersUtil.timeStamp))
									.queryParam("limit", 100)
									.queryParam("offset", offset)
									.build();
	}

	private HttpEntity<String> buildHeader() {
		var headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return new HttpEntity<>("parameters", headers);
	}
}
