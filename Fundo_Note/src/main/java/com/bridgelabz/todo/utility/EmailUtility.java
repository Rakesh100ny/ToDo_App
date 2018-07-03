package com.bridgelabz.todo.utility;


import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtility
{
    public static void sendEmail(String toAddress,String subject, String msg) throws AddressException,MessagingException 
    {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() 
        {
            public PasswordAuthentication getPasswordAuthentication() 
            {
                return new PasswordAuthentication("bankofjodhana@gmail.com", "adminrk100ni1900");
            }
        };
 
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message message = new MimeMessage(session);
 
            
        message.setFrom(new InternetAddress("bankofjodhana@gmail.com"));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        message.setRecipients(Message.RecipientType.TO, toAddresses);
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setText(msg);
 
        // sends the e-mail
        Transport.send(message);
    }
}