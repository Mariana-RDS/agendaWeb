package com.vespertinedev.agendaWeb.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/agenda_web";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    private static Connection conn = null;

    static Connection getCurrentConnection() throws SQLException{

        if (conn == null){
            try{
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        return conn;
    }

    static Connection getNewConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
