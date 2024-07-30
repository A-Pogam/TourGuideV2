package com.openclassrooms.tourguide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;

/**
 * Configuration class for setting up application modules.
 * This class defines beans for GPS Utility and Reward Central services.
 */
@Configuration
public class ModuleConfig {

	/**
	 * Creates and returns a GpsUtil bean.
	 * GpsUtil is used for handling GPS related operations.
	 *
	 * @return a new instance of GpsUtil.
	 */
	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

	/**
	 * Creates and returns a RewardCentral bean.
	 * RewardCentral is used for handling reward calculations.
	 *
	 * @return a new instance of RewardCentral.
	 */
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
}