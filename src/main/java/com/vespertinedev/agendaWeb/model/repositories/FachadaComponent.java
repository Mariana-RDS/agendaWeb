package com.vespertinedev.agendaWeb.model.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vespertinedev.agendaWeb.model.entity.ContatoEntity;
import org.springframework.stereotype.Component;

@Component
public class FachadaComponent {

    private GenericRepository<ContatoEntity, Integer> rContato =null;

    public FachadaComponent(){
        rContato = new ContatoRepository();
    }

    public void create(ContatoEntity c) throws SQLException{
        this.rContato.create(c);
    }
    public void update(ContatoEntity c) throws SQLException{
        this.rContato.update(c);
    }
    public ContatoEntity read(int id) throws SQLException{
        return this.rContato.read(id);
    }
    public void delete(int id) throws SQLException{
        this.rContato.delete(id);
    }
    public List<ContatoEntity> readAll(Integer usuarioId) throws SQLException {
        List<ContatoEntity> contatos = ((ContatoRepository) this.rContato).readAll(usuarioId);
        return (contatos != null) ? contatos : new ArrayList<>();
    }

}
