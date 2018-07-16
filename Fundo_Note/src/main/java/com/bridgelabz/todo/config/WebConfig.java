package com.bridgelabz.todo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.bridgelabz.todo")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
		
	@Override
	public void addCorsMappings(CorsRegistry registry) {
	  registry.addMapping("/**")
   	  .allowedOrigins("*")
		  .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
		  .allowCredentials(false)
		  .maxAge(4800);
	}

}
