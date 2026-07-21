package com.joaogabriel.dev.biblioteca.service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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

    public void sendMailLoanBook(String to, String nomeDestinatario, String nameBook, OffsetDateTime dueDate){
        SimpleMailMessage message = new SimpleMailMessage();

        String dateFormat = dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        message.setFrom(this.FROM);
        message.setTo(to);
        message.setSubject("Empréstimo realizado");
        message.setText(
            "Olá " +nomeDestinatario+ ",\n\n" +
            "Você realizou o empréstimo do livro " +nameBook+ ". \nO prazo de empréstimo é de 7 dias.\n"
            + "Data da devolução: " +dateFormat
            + "\n\nAtenciosamente, secretaria da biblioteca"
        );

        mailSender.send(message);
    }

    public void sendMailLoanReturn(String to, String nomeDestinatario, String nameBook){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.FROM);
        message.setTo(to);
        message.setSubject("Devolução realizada");
        message.setText(
            "Olá " +nomeDestinatario+ ",\n\n" +
            "Recebemos a devolução do livro " +nameBook+ ".\nAgredecemos sua responsabilidade e cuidado."
            + "\n\nAtenciosamente, secretaria da biblioteca"
        );

        mailSender.send(message);
    }
}
