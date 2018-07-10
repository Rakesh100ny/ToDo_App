package com.bridgelabz.todo.utility;


import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

public class EmailUtility
{
	@Autowired
	private Session session;
	
	public EmailUtility(Session session)
	{
	 this.session=session;	
	}
    
    public void sendEmail(String toAddress,String subject, String link) throws AddressException,MessagingException 
    {
        /*StringBuilder bodyText = new StringBuilder(); 
        bodyText.append("<div>")
             .append("  Dear User<br/><br/>")
             .append("  Thank you for registration. Your mail ("+toAddress+") is under verification<br/>")
             .append("  Please click here or open below link in browser "+link+"")
             .append("  <br/><br/>")
             .append("  Thanks,<br/>")
             .append("  Fundoo Team")
             .append("</div>");
        */
        // creates a new e-mail message
        Message message = new MimeMessage(session);
 
            
        message.setFrom(new InternetAddress("bankofjodhana@gmail.com"));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        message.setRecipients(Message.RecipientType.TO, toAddresses);
        message.setSubject(subject);
        message.setSentDate(new Date());
      //  message.setContent(bodyText.toString(),"text/html; charset=utf-8");
        message.setText(link);
        Transport.send(message);
 
        // sends the e-mail
        Transport.send(message);
    }
}