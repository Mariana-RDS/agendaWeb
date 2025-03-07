package com.vespertinedev.agendaWeb.model.repositories;

import java.sql.SQLException;
import java.util.List;

import com.vespertinedev.agendaWeb.model.entity.ContatoEntity;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class FachadaComponent {

    private Repository<ContatoEntity, Integer> rContato =null;

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
    public List<ContatoEntity> readAll() throws SQLException{
        return ((ContatoRepository)this.rContato).readAll();
    }
}
