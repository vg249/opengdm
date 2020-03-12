package org.gobiiproject.gobiimodel.utils.email;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import static org.gobiiproject.gobiimodel.utils.email.AuthType.PASSWORD;

public class MailInterface {
	
	private String host;
	private String port;
	private String user;
	private String password;
	private String protocol;
	private AuthType authType=PASSWORD;
	public static boolean noMail = false; // Flag for disabling email
	private String from;
	
	public MailInterface(ConfigSettings config) {
		host = config.getEmailSvrDomain();
		port = config.getEmailServerPort().toString();
		user = config.getEmailSvrUser();
		password = config.getEmailSvrPassword();
		protocol = config.getEmailSvrType().toLowerCase();
		try {
			authType = config.getEmailAuth();
		}catch(IllegalArgumentException e){
			Logger.logWarning("MailInterface","Unable to parse Email Auth. Defaulting to " +authType.name());
		}
		 from = config.getEmailSvrFrom();
	}
	
	public String getHost(){
		return host;
	}
	
	public String getPort(){
		return port;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getProtocol(){
		return protocol;
	}
	public AuthType getAuthType(){
		return authType;
	}

	
	public void send(MailMessage message ) throws Exception{
		if(noMail){
			Logger.logWarning("MailInterface","Ignoring all mail");
			return;
		}

		if(message.getUser()==null || message.getUser().equals("")) {
			Logger.logWarning("MailInterface", "User for email was empty. Not sending.");
			return;
		}
		
		String username = this.getUser();
		String password = this.getPassword();
		String protocol = this.getProtocol();
		AuthType authType = this.getAuthType();
		
		Properties props = new Properties();
		if(authType.equals(PASSWORD)) {
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.starttls.required", "true");
		}

		props.setProperty("mail.transport.protocol", protocol);
		props.setProperty("mail.smtp.host", this.getHost());
		props.setProperty("mail.smtp.port", this.getPort());
		props.setProperty("mail.host", this.getHost());
		props.setProperty("mail.port", this.getPort());
		props.setProperty("mail.user", username);

		if(authType.equals(PASSWORD)) {
			props.setProperty("mail.password", password);
		}

		Session mailSession = Session.getInstance(props,
				new javax.mail.Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication(){
						return new PasswordAuthentication(username, password);
					}
				});

		MimeMessage mimeMessage = new MimeMessage(mailSession);
		InternetAddress fromAddress;
		if(from != null && !from.equals("")) {//From is optional
			fromAddress=new InternetAddress(from);
		} else { //Default to from user equal to login username
			fromAddress=new InternetAddress(username);
		}
		mimeMessage.setFrom(fromAddress);
		mimeMessage.setSubject(message.getSubject());
		
		MimeMultipart multipart = new MimeMultipart("related");
		BodyPart messageBodyPart = new MimeBodyPart();
		String confidentialityContent = message.getConfidentialityMessage();
		if(confidentialityContent==null)confidentialityContent="";
		String htmlContent = message.getHeader() + message.getBody() + message.getFooter() + confidentialityContent;
		messageBodyPart.setContent(htmlContent, "text/html");
		multipart.addBodyPart(messageBodyPart);
		messageBodyPart = new MimeBodyPart();
		DataSource fds = new URLDataSource(message.getImg());
		messageBodyPart.setDataHandler(new DataHandler(fds));
		messageBodyPart.setHeader("Content-ID", "<image>");
		multipart.addBodyPart(messageBodyPart);

		// add attachments
		if(message.getFileAttachments().size() > 0) {

			for (String fileName : message.getFileAttachments()) {

				messageBodyPart = new MimeBodyPart();

				DataSource source = new FileDataSource(fileName);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(fileName);
				multipart.addBodyPart(messageBodyPart);

			}

		}


		mimeMessage.setContent(multipart);
		mimeMessage.addRecipient(Message.RecipientType.TO,
				new InternetAddress(message.getUser()));
		Logger.logDebug("Mail Interface","Recipient.TO => "+message.getUser());
		switch(authType){
			case PASSWORD:
				Transport transport = mailSession.getTransport(protocol);
				transport.connect(username, password);
				transport.sendMessage(mimeMessage,
						mimeMessage.getRecipients(Message.RecipientType.TO));
				Logger.logDebug("Mail Interface", "Sending To => " + mimeMessage.getRecipients(Message.RecipientType.TO));
				transport.close();
				Logger.logInfo("Mail Interface", "Email sent");
				break;
			case PASSWORDLESS:
				Transport.send(mimeMessage);
				Logger.logDebug("Mail Interface", "Sending To => " + mimeMessage.getRecipients(Message.RecipientType.TO));
				break;
		}
	}
	
}