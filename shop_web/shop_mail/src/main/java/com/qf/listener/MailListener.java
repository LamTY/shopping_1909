package com.qf.listener;

import com.qf.entity.Email;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class MailListener {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    @RabbitListener(queuesToDeclare = @Queue(name="mail_queue"))
    public void msgHandler(Email email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);

        mimeMessageHelper.setTo(email.getTo());
        mimeMessageHelper.setSubject(email.getSubject());
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setSentDate(new Date());
        mimeMessageHelper.setText(email.getContext(),true);

        javaMailSender.send(mimeMessage);
        System.out.println("发送成功");
    }
}
