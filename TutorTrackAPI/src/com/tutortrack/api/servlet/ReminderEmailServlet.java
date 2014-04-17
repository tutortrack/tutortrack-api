package com.tutortrack.api.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ReminderEmailServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6509639536314507492L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "You have a tutoring appointment in 2 hours.";

		try {
		    Message msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress("mail.tutortrack@gmail.com", "TutorTrack Mailer"));
		    msg.addRecipient(Message.RecipientType.TO,
		     new InternetAddress(email, name));
		    msg.setSubject("Appointment Reminder");
		    msg.setText(msgBody);
		    Transport.send(msg);

		} catch (AddressException e) {
		    // ...
		} catch (MessagingException e) {
		    // ...
		}
		
		response.sendRedirect("/");

	}

}
