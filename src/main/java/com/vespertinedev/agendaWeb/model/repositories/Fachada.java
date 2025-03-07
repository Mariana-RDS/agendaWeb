package com.vespertinedev.agendaWeb.model.repositories;

import java.sql.SQLException;
import java.util.List;

import com.vespertinedev.agendaWeb.model.entity.ContatoEntity;
import com.vespertinedev.agendaWeb.model.entity.UsuarioEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Fachada {

    private static Fachada myself = null;

    private Repository<ContatoEntity, Integer> rContato = null;
    private Repository<UsuarioEntity, Integer> rUsuario = null;

    public static Fachada getCurrentInstance(){
        if(myself == null){
            myself = new Fachada();
        }
        return myself;
    }

    /*
     * Contato
     */

    public void create(ContatoEntity c) throws SQLException{
        this.rContato.create(c);
    }
    public void update(ContatoEntity c) throws SQLException{
        this.rContato.update(c);
    }
    public ContatoEntity readContato(int id) throws SQLException{
        return this.rContato.read(id);
    }
    public void deleteContato(int id) throws SQLException{
        this.rContato.delete(id);
    }
    public List<ContatoEntity> readAll() throws SQLException{
        List<ContatoEntity> contatos = ((ContatoRepository)this.rContato).readAll();
        return contatos;
    }

    /*
     * Usuario
     */

    public void create(UsuarioEntity u) throws SQLException{
        this.rUsuario.create(u);
    }
    public void update(UsuarioEntity u) throws SQLException{
        this.rUsuario.update(u);
    }
    public UsuarioEntity readUsuario(int id) throws SQLException{
        return this.rUsuario.read(id);
    }
    public void deleteUsuario(int id) throws SQLException{
        this.rUsuario.delete(id);
    }
}
