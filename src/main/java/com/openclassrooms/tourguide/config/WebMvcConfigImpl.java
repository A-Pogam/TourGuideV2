package com.openclassrooms.tourguide.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.openclassrooms.tourguide.listener.EndpointListener;

@Configuration
public class WebMvcConfigImpl implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new EndpointListener());
	}
}