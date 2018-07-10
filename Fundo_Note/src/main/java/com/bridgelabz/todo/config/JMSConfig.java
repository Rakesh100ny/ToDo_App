package com.bridgelabz.todo.config;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;

import com.bridgelabz.todo.userservice.jms.MessageSender;
import com.bridgelabz.todo.userservice.model.EmailModel;
import com.bridgelabz.todo.utility.EmailUtility;

@Configuration
@PropertySource("classpath:mail.properties")
public class JMSConfig {
	@Value("${mail.transport.protocol}")
	private String protocol;

	@Value("${mail.host}")
	private String host;

	@Value("${mail.port}")
	private String port;

	@Value("${mail.smtp.auth}")
	private String auth;

	@Value("${mail.smtp.starttls.enable}")
	private String starttls;

	@Value("${mail.user}")
	private String userName;

	@Value("${mail.pass}")
	private String password;

	private final String DEFAULT_BROKER_URL = "tcp://localhost:61616";
	private final String MAIL_QUEUE = "mail.queue";

	@Bean
	public ActiveMQConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
		connectionFactory.setTrustedPackages(Arrays.asList("com.bridgelabz.todo.userservice"));
		return connectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory());
		jmsTemplate.setDefaultDestinationName(MAIL_QUEUE);
		return jmsTemplate;
	}

	@Bean
	public EmailModel getEmailModel() {
		return new EmailModel();
	}

	@Bean
	public MessageSender getMessageSender() {
		return new MessageSender();
	}

	@Bean
	public Session getSession() {
		// We could be more flexible and have auth based on whether there's a username
		// and starttls based on a property.
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", Integer.parseInt(port));
		properties.put("mail.transport.protocol", protocol);
		properties.put("mail.smtp.auth", auth);
		properties.put("mail.smtp.starttls.enable", starttls);

		return Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
	}
	
	@Bean
	public EmailUtility EmailUtility() {
		return new EmailUtility(getSession());
	}

}
