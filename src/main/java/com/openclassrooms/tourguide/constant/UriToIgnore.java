package com.openclassrooms.tourguide.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UriToIgnore {

	public static final String ERROR_URI = "/error";

	public static final List<String> URI_TO_IGNORE = new ArrayList<>(Arrays.asList(ERROR_URI));
}