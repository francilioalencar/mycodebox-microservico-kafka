package com.mycodebox.microservico.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycodebox.microservico.dto.Contato;
import com.mycodebox.microservico.dto.ContatoDto;

@Service
public class MailService {



    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender){
        this.mailSender = mailSender;

    }

    @Value("${spring.mail.username}")
    String from;


    @KafkaListener(topics = "enviar-contato", groupId = "mycodebox-enviar-contato")
    public void processaTopico( ConsumerRecord<String, String> contatoDto){

        System.out.println( "--------------------");
        System.out.println( "--------------------");
        System.out.println( contatoDto.value());

        ObjectMapper    mapper = new ObjectMapper();
        ContatoDto      dto;
        String str = contatoDto.value();
        try{
            dto = mapper.readValue(str, ContatoDto.class);
            enviarEmail(dto);
        } catch (JsonProcessingException ex) {
            System.out.println("Erro:::"+ex);
        }
        

    }

        
        private void enviarEmail(ContatoDto contato) {
        System.out.println("From:............:"+from);
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(contato.email()); // pode ser dinâmico se quiser
        email.setSubject(contato.assunto());
        email.setText("Mensagem recebida: " + contato.mensagem());
        email.setFrom(from);

        mailSender.send(email);
    }
    
}
