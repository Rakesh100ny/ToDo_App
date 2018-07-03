package com.bridgelabz.todo.config;

import java.util.Arrays;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import com.bridgelabz.todo.userservice.jms.MessageSender;
import com.bridgelabz.todo.userservice.model.EmailModel;

@Configuration
public class JMSConfig 
{
 private final String DEFAULT_BROKER_URL="tcp://localhost:61616";
 private final String MAIL_QUEUE="mail.queue";
	
 
 @Bean
 public ActiveMQConnectionFactory connectionFactory()
 {
  ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory();
  connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
  connectionFactory.setTrustedPackages(Arrays.asList("com.bridgelabz.todo.userservice"));
  return connectionFactory;
 }
 
 
 @Bean
 public JmsTemplate jmsTemplate()
 {
  JmsTemplate jmsTemplate=new JmsTemplate();
  jmsTemplate.setConnectionFactory(connectionFactory());
  jmsTemplate.setDefaultDestinationName(MAIL_QUEUE);
  return jmsTemplate;
 }
 
 @Bean 
 public EmailModel getEmailModel()
 {
  return new EmailModel();	 
 }
 
 @Bean
 public MessageSender getMessageSender()
 {
  return new MessageSender(); 	 
 }
}
