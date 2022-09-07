package com.growin.marvelapi.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class CharactersUtil {
	
	private CharactersUtil() {
	}

	public static long timeStamp = Instant.now().toEpochMilli();

	public static String md5hash(String publicApiKey, String privateApiKey, Long timestamp) {
		try {
			var md = MessageDigest.getInstance("MD5");
			var toHash = timestamp + privateApiKey + publicApiKey;
			return new BigInteger(1, md.digest(toHash.getBytes())).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.getMessage();
		}

		return null;
	}
}
