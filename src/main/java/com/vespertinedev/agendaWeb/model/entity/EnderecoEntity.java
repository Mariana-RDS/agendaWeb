package com.vespertinedev.agendaWeb.model.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EnderecoEntity {

    private int id;
    private String rua;
    private String cidade;
    private String estado;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getRua(){
        return rua;
    }

    public void setRua(String rua){
        this.rua = rua;
    }

    public String getCidade(){
        return cidade;
    }

    public void setCidade(String Cidade){
        this.cidade = cidade;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }
}
