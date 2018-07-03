package com.bridgelabz.todo.userservice.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.bridgelabz.todo.userservice.model.EmailModel;

@Component
public class MessageSender{

	@Autowired
	JmsTemplate template;

	public void sendMessage(final EmailModel emailModel) {
		template.send(new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage objectMessage=session.createObjectMessage(emailModel);
				
				return objectMessage;
			}
		});
	}
}
