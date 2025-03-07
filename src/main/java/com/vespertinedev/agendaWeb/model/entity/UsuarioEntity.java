package com.vespertinedev.agendaWeb.model.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    private int id;
    private String nome;
    private String username;
    private String password;
    private String telefone_num;
    private EnderecoEntity enderecoEntity;
    private List<ContatoEntity> contatos = new ArrayList<>();

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getTelefoneNum(){
        return telefone_num;
    }

    public void setTelefoneNum(String telefone_num){
        this.telefone_num = telefone_num;
    }

    public EnderecoEntity getEnderecoEntity(){
        return enderecoEntity;
    }

    public void setEnderecoEntity(EnderecoEntity enderecoEntity){
        this.enderecoEntity = enderecoEntity;
    }

    public List<ContatoEntity> getContatos(){
        return contatos;
    }

    public void setContatos(List<ContatoEntity> contatos){
        this.contatos = contatos;
    }

}
