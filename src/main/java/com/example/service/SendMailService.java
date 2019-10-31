package com.example.service;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.example.domain.Order;

/**
 * メール関連機能の業務処理を行うサービス
 * @author juri.saito
 *
 */
@Component
@Transactional
public class SendMailService {

	 private static final Logger log = LoggerFactory.getLogger(SendMailService.class);

	  private final JavaMailSender javaMailSender;

	  @Autowired
	  SendMailService(JavaMailSender javaMailSender) {
	    this.javaMailSender = javaMailSender;
	  }

	  /**
	 * メール送信する
	 */
	  public void sendMail(Order order) {
			
		javaMailSender.send(new MimeMessagePreparator() {

	        @Override
	        public void prepare(MimeMessage mimeMessage) throws Exception {
	        	Context context = new Context();
	    		context.setVariable("name", order.getDestinationName());
	    		context.setVariable("order", order);
	    		context.setVariable("bootstrap", "../../static/css/bootstrap.css");
	    		context.setVariable("imgCurry", "/img_curry/");
	    		String email = order.getDestinationEmail();
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
	            helper.setFrom(System.getenv("GoogleMail"));
	            helper.setTo(email);
	            helper.setSubject("【ラクラクカリー】ご注文完了のお知らせ");
	            helper.setText(getMailBody("finished_confirm", context), true);
	        }
	    });
	}
	  
	/**
	 * Thymeleafのテンプレートを使用してメールの本文を作成する
	 * @param templateName
	 * @param context
	 * @return
	 */
	private String getMailBody(String templateName, Context context) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(mailTemplateResolver());
		return templateEngine.process(templateName, context);
	}
	
	private ClassLoaderTemplateResolver mailTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("mailtemplate/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(true);
		return templateResolver;
	}
}

