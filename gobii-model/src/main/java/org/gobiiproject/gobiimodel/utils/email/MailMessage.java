package org.gobiiproject.gobiimodel.utils.email;

public class MailMessage{
	
	private String user;
	private String subject;
	private String body;
	private String header;
	private String footer;
	private String img = "http://cbsugobii05.tc.cornell.edu/wordpress/wp-content/uploads/2015/11/GOBII-tm-e1448284547470.png"; // Note: This is full sized logo from the main page
	
	MailMessage(){
		header = "<b>Good day!</b><br/> Here is a summary of your transaction: <br/><br/>";
		footer = "<br/><br/>Cheers, <br/> <img src=\"cid:image\" width=\"300\">";
	}
	
	public String getUser(){
		return user;
	};
	
	public MailMessage setUser(String user){
		this.user = user;
		return this;
	}
	
	public String getHeader(){
		return header;
	}
	
	public String getImg(){
		return img;
	}
	
	public String getFooter(){
		return footer;
	}
	
	public String getSubject(){
		return subject;
	}
	
	public MailMessage setSubject(String subject){
		this.subject = subject;
		return this;	
	}
	
	public String getBody(){
		return body;
	}
	
	public MailMessage setBody(String body){
		this.body = body;
		return this;
	}
}