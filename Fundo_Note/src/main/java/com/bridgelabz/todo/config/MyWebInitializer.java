package com.bridgelabz.todo.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	 @Override
	   protected Class<?>[] getRootConfigClasses() {
	      return new Class[] { HibernateConfig.class,JMSListnerConfiguration.class,JMSConfig.class,RedisConfig.class };
	   }

	   @Override
	   protected Class<?>[] getServletConfigClasses() {
	      return new Class[] { WebConfig.class };
	   }

	   @Override
	   protected String[] getServletMappings() {
		   System.out.println("rakesh");
	      return new String[]{ "/" };
	   }
	}