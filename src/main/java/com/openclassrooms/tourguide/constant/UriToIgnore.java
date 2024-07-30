package com.openclassrooms.tourguide.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class that holds URIs to be ignored by certain processes within the TourGuide application.
 */
public class UriToIgnore {

	/**
	 * URI for error handling.
	 */
	public static final String ERROR_URI = "/error";

	/**
	 * List of URIs to be ignored.
	 * Currently, it contains only the error URI.
	 */
	public static final List<String> URI_TO_IGNORE = new ArrayList<>(Arrays.asList(ERROR_URI));
}