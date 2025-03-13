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
    private String rua;
    private String cidade;
    private String estado;
    private UsuarioEntity usuarioEntity;
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

    public UsuarioEntity getUsuarioEntity(){ return usuarioEntity; }
    public void setUsuarioEntity(UsuarioEntity usuarioEntity){ this.usuarioEntity = usuarioEntity; }

    public String getRua(){ return rua; }
    public void setRua(String rua){ this.rua = rua; }

    public String getCidade(){ return cidade; }
    public void setCidade(String cidade){ this.cidade = cidade; }

    public String getEstado(){ return estado; }
    public void setEstado(String estado){ this.estado = estado;}

    public List<TelefoneEntity> getTelefones(){
        return telefones;
    }
    public void setTelefoneEntity(List<TelefoneEntity> telefones){
        this.telefones = telefones;
    }


}
