package com.bridgelabz.todo.userservice.jms;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.bridgelabz.todo.userservice.model.EmailModel;
import com.bridgelabz.todo.utility.EmailUtility;

@Component
public class MessageReceiver {
	private final String EMAIL_RESPONSE_QUEUE = "mail.queue";

	@Autowired
	EmailUtility EmailUtility;
	
	
	
	@JmsListener(destination = EMAIL_RESPONSE_QUEUE)
	public void receiverMessage(final Message<EmailModel> message) {
		EmailModel emailModel = message.getPayload();

		System.out.println("Mail-Id : " + emailModel.getTo());
		System.out.println("Subject : " + emailModel.getSubject());
		System.out.println("Link    : " + emailModel.getUrl());

		try {

			EmailUtility.sendEmail(emailModel.getTo(), emailModel.getSubject(), emailModel.getUrl());
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
