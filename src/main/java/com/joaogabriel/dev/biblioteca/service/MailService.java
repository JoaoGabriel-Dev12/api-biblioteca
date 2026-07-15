package com.joaogabriel.dev.biblioteca.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final String FROM = "amorimjoaogabriel789@gmail.com";

    public MailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendMailCreateAccount(String to, String nomeDestinatario){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.FROM);
        message.setTo(to);
        message.setSubject("Cadastro realizado");
        message.setText(
            "Olá " +nomeDestinatario+ ",\n\n" +
            "Seu cadastro foi realizado com êxito na biblioteca Jose Candido,\nRua A, Bairro Setor Central.\n"
            + "Atenciosamente, secretaria da biblioteca"
        );

        mailSender.send(message);
    }
}
