/**
 * 
 */
package com.growin.marvelapi.integration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.growin.marvelapi.exception.MarvelApiException;
import com.growin.marvelapi.exception.ObjectNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Guilherme Vital
 *
 */
@Service
@Slf4j
public class TranslatorClientImpl implements TranslatorClient {

	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${googleScriptUrl}")
	public String googleScriptUrl;

	@Override
	public String translate(String description, Locale languageCode) {
		log.info("Retrieving the translation to {}", languageCode);

		String encode = buildEncode(description);
		
		UriComponents uri = UriComponentsBuilder.newInstance().scheme("https").host(googleScriptUrl).path("/exec")
												.queryParam("q", encode)
												.queryParam("source", Locale.ENGLISH)
												.queryParam("target", languageCode)
												.build();
		try {
			var body = restTemplate.exchange(uri.toString(), HttpMethod.GET, buildHeader(), String.class).getBody();
			log.info("Translated text: {}", body);
			
			return body.replaceAll("%2C", ",").replaceAll("%21", "!");
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == 404) {
				throw new ObjectNotFoundException("The character was not found");
			}
			return null;
		}
	}

	private String buildEncode(String description) {
		try {
			return URLEncoder.encode(description, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			throw new MarvelApiException("Problem with encoding...");
		}
	}

	private HttpEntity<String> buildHeader() {
		var headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return new HttpEntity<>("parameters", headers);
	}
}
