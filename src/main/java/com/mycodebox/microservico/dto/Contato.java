package com.mycodebox.microservico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Contato {
    
   private  String email;
   private   String telefone;
   private   String mensagem;
   private   String assunto;
   private   String tipoMensagem;


}
