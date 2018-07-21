package com.bridgelabz.todo.config;

import static org.hibernate.cfg.Environment.*;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.userservice.model.User;

@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScan("com.bridgelabz.todo")
public class HibernateConfig {

	@Autowired
	private Environment environment;

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

		Properties props = new Properties();
		// Setting JDBC properties

		props.put(DRIVER, environment.getProperty("mysql.driver"));
		props.put(URL, environment.getProperty("mysql.url"));
		props.put(USER, environment.getProperty("mysql.user"));
		props.put(PASS, environment.getProperty("mysql.password"));

		// Setting Hibernate properties
		props.put(SHOW_SQL, environment.getProperty("hibernate.show_sql"));
		props.put(HBM2DDL_AUTO, environment.getProperty("hibernate.hbm2ddl.auto"));
        props.put(USE_SECOND_LEVEL_CACHE,"hibernate.cache.use_second_level_cache");
	    props.put(CACHE_REGION_FACTORY,"hibernate.cache.provider_class");
      
		
		// Setting C3P0 properties
		props.put(C3P0_MIN_SIZE, environment.getProperty("hibernate.c3p0.min_size"));
		props.put(C3P0_MAX_SIZE, environment.getProperty("hibernate.c3p0.max_size"));
		props.put(C3P0_ACQUIRE_INCREMENT, environment.getProperty("hibernate.c3p0.acquire_increment"));
		props.put(C3P0_TIMEOUT, environment.getProperty("hibernate.c3p0.timeout"));
		props.put(C3P0_MAX_STATEMENTS, environment.getProperty("hibernate.c3p0.max_statements"));
		factoryBean.setHibernateProperties(props);
		
		factoryBean.setPackagesToScan("com.bridgelabz.todo.userservice.model");
		factoryBean.setPackagesToScan("com.bridgelabz.todo.noteservice.model");
		factoryBean.setPackagesToScan("com.bridgelabz.todo.label.model");
		factoryBean.setAnnotatedClasses(User.class,Note.class,Label.class);
		

		return factoryBean;
	}

	@Bean
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(getSessionFactory().getObject());
		return transactionManager;
	}

	@Bean
	public User getUser() {
		return new User();
	}
	
	
}