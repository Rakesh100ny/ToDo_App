package com.bridgelabz.todo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages ="com.bridgelabz.todo")
@EnableWebMvc

public class WebConfig implements WebMvcConfigurer {

/*		@Override
	    public void configureViewResolvers(ViewResolverRegistry registry) {
	        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
	        viewResolver.setPrefix("/webapp/");
	        viewResolver.setSuffix(".html");
	        registry.viewResolver(viewResolver);
	    }
	 
		
	    @Override
	    public void addViewControllers(ViewControllerRegistry registry) {

	    }*/


	    @Override
	    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	        configurer.enable();
	    }
	   
	 

}
