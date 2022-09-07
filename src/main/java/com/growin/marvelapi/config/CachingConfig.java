package com.growin.marvelapi.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Guilherme Vital
 *
 */
@Configuration
@EnableScheduling
public class CachingConfig {
	public static final String MARVEL_API = "marvel-api-characters";
	
	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager(MARVEL_API);
	}
}