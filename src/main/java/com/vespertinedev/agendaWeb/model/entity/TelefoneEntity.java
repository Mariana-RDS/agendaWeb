package com.vespertinedev.agendaWeb.model.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TelefoneEntity {

    private int id;
    private String numero;
    private ContatoEntity contatoEntity;

    public int getId(){
        return id;
    }

    public void setId(int id){
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
