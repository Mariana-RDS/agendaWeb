package com.vespertinedev.agendaWeb.model.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ContatoEntity {

    private Integer id;
    private String nome;
    private String email;
    private UsuarioEntity usuarioEntity;
    private EnderecoEntity enderecoEntity;
    private List<TelefoneEntity> telefones = new ArrayList<>();

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public UsuarioEntity getUsuarioEntity(){

        return usuarioEntity;
    }

    public void setUsuarioEntity(UsuarioEntity usuarioEntity){

        this.usuarioEntity = usuarioEntity;
    }

    public EnderecoEntity getEnderecoEntity(){

        return enderecoEntity;
    }

    public void setEnderecoEntity(EnderecoEntity enderecoEntity){

        this.enderecoEntity = enderecoEntity;
    }

    public List<TelefoneEntity> getTelefones(){
        return telefones;
    }

    public void setTelefoneEntity(List<TelefoneEntity> telefones){
        this.telefones = telefones;
    }
}
