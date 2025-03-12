package com.vespertinedev.agendaWeb.model.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    private Integer id;
    private String nome;
    private String username;
    private String password;
    private String telefone_num;
    private String rua;
    private String cidade;
    private String estado;
    private List<ContatoEntity> contatos = new ArrayList<>();

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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<ContatoEntity> getContatos(){
        return contatos;
    }

    public void setContatos(List<ContatoEntity> contatos){
        this.contatos = contatos;
    }

}
