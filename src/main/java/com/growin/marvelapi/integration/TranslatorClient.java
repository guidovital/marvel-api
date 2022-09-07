/**
 * 
 */
package com.growin.marvelapi.integration;

import java.util.Locale;

/**
 * @author Guilherme Vital
 *
 */
public interface TranslatorClient {

	String translate(String description, Locale languageCode);

}
