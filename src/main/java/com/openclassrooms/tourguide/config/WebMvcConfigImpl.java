package com.openclassrooms.tourguide.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.openclassrooms.tourguide.listener.EndpointListener;

/**
 * Web MVC Configuration class for the TourGuide application.
 * This class configures the application's interceptors.
 */
@Configuration
public class WebMvcConfigImpl implements WebMvcConfigurer {

	/**
	 * Adds custom interceptors to the application's interceptor registry.
	 * In this case, it adds an {@link EndpointListener} interceptor.
	 *
	 * @param registry the {@link InterceptorRegistry} to which the interceptor is added.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new EndpointListener());
	}
}