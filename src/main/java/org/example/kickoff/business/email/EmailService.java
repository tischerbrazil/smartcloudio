package org.example.kickoff.business.email;

import static org.example.kickoff.business.email.EmailTemplate.EmailTemplatePart.BODY_TITLE;
import static org.example.kickoff.business.email.EmailTemplate.EmailTemplatePart.SUBJECT_CONTENT;
import static org.omnifaces.utils.Lang.isEmpty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.example.kickoff.business.email.EmailTemplate.EmailTemplatePart;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public abstract class EmailService  {

	private static final String LOG_MAIL_FAIL = "Failed to send mail [type=%s, to=%s, replyTo=%s, subject=%s]";
	private static final List<String> ALLOWED_FROM_DOMAINS = Arrays.asList("smartcloudio.com.br");

	private EmailUser defaultEmailUser;

	//@Inject @ApplicationSetting
	private String fromEmail = "webmaster@smartcloudio.com.br"; //FIXME

	//@Inject @ApplicationSetting
	private Boolean disableEmailService = false; //FIXME

	@Inject
	private EmailTemplateService emailTemplateService;

	@PostConstruct
	public void init() {
		
		//HERE LOAD EMAIL...................................................... FIXME
		defaultEmailUser = new EmailUser(fromEmail, "The Risteos Project");
	}

	public void sendTemplate(EmailTemplate templateEmail) {
		sendTemplate(templateEmail, new HashMap<>());
	}

	public String sendTemplate(EmailTemplate templateEmail, Map<String, Object> messageParameters) {
		if (disableEmailService) {
//			return;
		}

		if (templateEmail.getFromUser() == null) {
			templateEmail.setFromUser(defaultEmailUser);
		}
		else { // Prevent sending email from other domains.
			EmailUser fromUser = templateEmail.getFromUser();
			String email = fromUser.getEmail();

			if (email != null && email.contains("@") && !ALLOWED_FROM_DOMAINS.contains(email.substring(email.indexOf("@") + 1))) {
				templateEmail.setFromUser(new EmailUser(defaultEmailUser.getEmail(), fromUser.getFullName() + " via Jakarta EE Kickoff App"));
				templateEmail.setReplyTo(fromUser.getEmail());
			}
		}

		if (templateEmail.getToUser() == null) {
			templateEmail.setToUser(defaultEmailUser);
		}

		emailTemplateService.addUserParameters("toUser", templateEmail.getToUser(), messageParameters);
		emailTemplateService.addUserParameters("fromUser", templateEmail.getFromUser(), messageParameters);

		try {
			Map<String, String> templateContent = buildTemplateContent(templateEmail, messageParameters);
			sendTemplateMessage(templateEmail, messageParameters, templateContent);
			
			
			System.out.println("---toUser--------------" + messageParameters.get("toUser"));
			System.out.println("---toUser.fullName-----" + messageParameters.get("toUser.fullName"));
			System.out.println("---fromUser.fullName---" + messageParameters.get("fromUser.fullName"));
			System.out.println("---toUser.email--------" + messageParameters.get("toUser.email"));
			System.out.println("---toUser.id-----------" + messageParameters.get("toUser.id"));
			System.out.println("---fromUser------------" + messageParameters.get("fromUser"));
			System.out.println("---fromUser.id---------" + messageParameters.get("fromUser.id"));
			System.out.println("---fromUser.email------" + messageParameters.get("fromUser.email"));
				
			System.out.println("---object_content------------" + 	templateContent.get("object_content"));
			
			System.out.println("---body_content--------------" + 	templateContent.get("body_content"));
			
			System.out.println("---call_to_action_label------" + 	templateContent.get("call_to_action_label"));
						
			System.out.println("---subject_content------------" + 	templateContent.get("subject_content"));
						
			System.out.println("---body_title-----------------" + 	templateContent.get("body_title"));
			
			System.out.println("---salutation_content---------" + 	templateContent.get("salutation_content"));
						
			System.out.println("--CallToActionURL()-----------" + 	templateEmail.getCallToActionURL());
			
			System.out.println("--getReplyTo------------------" + 	templateEmail.getReplyTo());
			System.out.println("--getTemplateId---------------" + 	templateEmail.getTemplateId());
			System.out.println("--getType---------------------" + 	templateEmail.getType());
			System.out.println("--getFromUser-----------------" + 	templateEmail.getFromUser());
			System.out.println("--getTemplateParts------------" + 	templateEmail.getTemplateParts());
			System.out.println("--getToUser()-----------------" + 	templateEmail.getToUser());
							
			String titleBody = "<strong>" + templateContent.get("salutation_content").toString() + "</strong><br/><br/>";
			String contentBody = "<strong>" + templateContent.get("body_content").toString() + "</strong><br/><br/>";
			String buttomReset = "<a href=\"" + templateEmail.getCallToActionURL() + "\">Clique aqui para redefinir sua senha</a>";
			
			String fullBody = titleBody + contentBody + buttomReset;
			System.out.println("--HTML---------------" + fullBody);
			
			testSend(messageParameters.get("toUser.email").toString(), 
					 messageParameters.get("fromUser.email").toString(),
					 templateContent.get("subject_content").toString(),
					 fullBody);
			
				
		
		}
		catch (Exception e) {
			String errorMessage = String.format(LOG_MAIL_FAIL, templateEmail.getType(), templateEmail.getToUser().getEmail(), templateEmail.getReplyTo(), templateEmail.getTemplateParts().get(SUBJECT_CONTENT));
			return errorMessage;
		}
		return "Email enviado com sucesso!";
	}

	public abstract void sendTemplateMessage(EmailTemplate templateEmail, Map<String, Object> messageParameters, Map<String, String> templateContent);



	public abstract void sendPlainTextMessage(EmailUser to, EmailUser from, String subject, String body, String replyTo);

	private Map<String, String> buildTemplateContent(EmailTemplate templateEmail, Map<String, Object> messageParameters) {
		Map<EmailTemplatePart, String> templateParts = templateEmail.getTemplateParts();
		Arrays.stream(EmailTemplatePart.values())
			.filter(part -> !templateParts.containsKey(part.getKey()))
			.forEach(part -> templateParts.putIfAbsent(part, emailTemplateService.build(templateEmail, part, messageParameters)));

		if (isEmpty(templateParts.get(BODY_TITLE))) {
			String subjectContent = templateParts.get(SUBJECT_CONTENT);
			templateParts.put(BODY_TITLE, subjectContent);
		}

		Map<String, String> templateContent = new HashMap<>();
		templateEmail.getTemplateParts().keySet().stream()
			.forEach(key -> templateContent.put(key.getKey(), templateEmail.getTemplateParts().get(key)));

		return templateContent;
	}
	
	
	
	
	 private  void testSend(String toAddress, String fromAddress, String subjectMessage, String textMessage) throws MessagingException {
	        String hostname = "smartcloudio.com.br";
	        final String username = "webmaster@smartcloudio.com.br";
	        final String password = "Lololo09*";
	        System.out.println("===========toAddress================ " + toAddress);


	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "false");
	        props.put("mail.smtp.host", hostname);
	        props.put("mail.smtp.port", "587");
	        props.put("mail.debug", "true");

	        jakarta.mail.Authenticator auth = new jakarta.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
	        };

	        Session session = Session.getInstance(props, auth);

	      
	            MimeMessage msg = new MimeMessage(session);
	            msg.setFrom(new InternetAddress(fromAddress));
	            
	            InternetAddress[] address = {new InternetAddress(toAddress)};
	            
	            
	            
	            msg.setRecipients(Message.RecipientType.TO, address);
	            msg.setSubject(subjectMessage);
	            msg.addHeader("x-cloudmta-class", "standard");
	            msg.addHeader("x-cloudmta-tags", "GOD IS VERY TRUST");
	            msg.setText(textMessage);

	            msg.setContent(textMessage, "text/html");
	            
	            Transport.send(msg);

	            System.out.println("Message Sent.");
	     
	        
	        
	    }
	
	
	
	
	
	
	

}