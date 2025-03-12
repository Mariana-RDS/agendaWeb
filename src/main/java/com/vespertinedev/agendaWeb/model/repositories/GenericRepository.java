package com.vespertinedev.agendaWeb.model.repositories;

import java.sql.SQLException;

public interface GenericRepository <CLAZZ,KEY>{

        public void create(CLAZZ z) throws SQLException;
        public void update(CLAZZ z) throws SQLException;
        public CLAZZ read(KEY k) throws SQLException;
        public void delete(KEY k) throws SQLException;

}
