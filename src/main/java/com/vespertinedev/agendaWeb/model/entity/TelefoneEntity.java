package com.vespertinedev.agendaWeb.model.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TelefoneEntity {

    private Integer id;
    private String numero;
    private ContatoEntity contatoEntity;
    private Integer id_contato;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getNumero(){
        return numero;
    }

    public void setNumero(String numero){
        this.numero = numero;
    }



    public ContatoEntity getContatoEntity(){
        return contatoEntity;
    }

    public void setContatoEntity(ContatoEntity contatoEntity){
        this.contatoEntity = contatoEntity;
    }
}
