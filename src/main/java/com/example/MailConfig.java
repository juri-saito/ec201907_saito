package com.example;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration 
public class MailConfig {


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");// ホスト名
        mailSender.setPort(25);// ポート番号

        mailSender.setUsername("test@test.co.jp");
        mailSender.setPassword("abc");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");// ユーザー認証
//        props.put("mail.smtp.starttls.enable", "true");// 暗号化(TLS保護接続への接続)
        props.put("mail.debug", "true");// デバッグ情報出力有無

        return mailSender;
    }
}



//mailSender.setHost("smtp.gmail.com");
//mailSender.setPort(587);
//mailSender.setPassword(System.getenv("GoogleMail"));
//mailSender.setPassword(System.getenv("GooglePass"));

//SMTP4dev使用・下記Server設定にチェックを入れる
//8BITMIME(Don't corrupt non ASCII data)
//STARTTLS(Explicit SSL/TLS)
//AUTH(Allow authentication -any credentials accepted)