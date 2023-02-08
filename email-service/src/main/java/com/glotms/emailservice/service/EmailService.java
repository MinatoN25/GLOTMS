package com.glotms.emailservice.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.glotms.userservice.dto.MailRequest;
import com.glotms.userservice.dto.MailResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;



@Service
public class EmailService {

	private JavaMailSender sender;

	@Autowired
	public EmailService(JavaMailSender sender) {
		this.sender = sender;
	}

	@Autowired
	private Configuration config;

	public MailResponse sendEmail(MailRequest request, Map<String, Object> model, String template) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			Template t = config.getTemplate(template);
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			helper.setSubject(request.getSubject());
			helper.setTo(request.getUserEmail());
			helper.setText(html, true);
			sender.send(message);

			response.setMessage("mail send to : " + request.getUserEmail());
			response.setStatus(Boolean.TRUE);

		} catch (MessagingException | IOException | TemplateException e) {
			response.setMessage("Mail Sending failure : " + e.getMessage());
			response.setStatus(Boolean.FALSE);
		}

		return response;
	}

}
