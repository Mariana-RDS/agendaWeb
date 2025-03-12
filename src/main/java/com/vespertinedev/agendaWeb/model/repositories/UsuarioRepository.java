package com.vespertinedev.agendaWeb.model.repositories;

import com.vespertinedev.agendaWeb.model.entity.UsuarioEntity;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;
import org.springframework.stereotype.Repository;

@Repository
public final class UsuarioRepository implements GenericRepository<UsuarioEntity, Integer>{

    protected UsuarioRepository(){}



    @Override
    public void create(UsuarioEntity z) throws SQLException {
        String sqlUsuario = "INSERT INTO Usuario (nome, username, password, telefone_num, rua, cidade, estado) VALUES(?,?,?,?,?,?,?)";


        try (Connection conn = ConnectionManager.getCurrentConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement pstmUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                pstmUsuario.setString(1, z.getNome());
                pstmUsuario.setString(2, z.getUsername());
                pstmUsuario.setString(3, z.getPassword());
                pstmUsuario.setString(4, z.getTelefoneNum());
                pstmUsuario.setString(5, z.getRua());
                pstmUsuario.setString(6, z.getCidade());
                pstmUsuario.setString(7, z.getEstado());
                pstmUsuario.executeUpdate();

                try (ResultSet generatedKeys = pstmUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        z.setId(generatedKeys.getInt(1));
                    }
                }

                conn.commit();

            } catch (SQLException e) {

                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                e.printStackTrace();
                throw e;
            } finally {

                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    @Override
    public void update(UsuarioEntity z) throws SQLException {
        String sql = "UPDATE Usuario SET nome=?, username=?, password=?, telefone_num=?, rua=?, cidade=?, estado=? WHERE id=?";

        try (Connection conn = ConnectionManager.getCurrentConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, z.getNome());
            pstm.setString(2, z.getUsername());
            pstm.setString(3, z.getPassword());
            pstm.setString(4, z.getTelefoneNum());
            pstm.setString(5, z.getRua());
            pstm.setString(6, z.getCidade());
            pstm.setString(7, z.getEstado());
            pstm.setInt(8, z.getId());

            pstm.executeUpdate();
        }
    }

    @Override
    public UsuarioEntity read(Integer k) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE id = ?";
        try (Connection conn = ConnectionManager.getCurrentConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, k);
            ResultSet result = pstm.executeQuery();

            UsuarioEntity u = null;

            if (result.next()) {
                u = new UsuarioEntity();
                u.setId(result.getInt("id"));
                u.setNome(result.getString("nome"));
                u.setUsername(result.getString("username"));
                u.setPassword(result.getString("password"));
                u.setTelefoneNum(result.getString("telefone_num"));
                u.setRua(result.getString("rua"));
                u.setCidade(result.getString("cidade"));
                u.setEstado(result.getString("estado"));
            }
            return u;
        }
    }

    public void delete(Integer k) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE id = ?";

        try (Connection conn = ConnectionManager.getCurrentConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, k);
            pstm.executeUpdate();
        }
    }

    public UsuarioEntity findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE username = ?";

        try (Connection conn = ConnectionManager.getCurrentConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, username);
            ResultSet result = pstm.executeQuery();

            if (result.next()) {
                UsuarioEntity u = new UsuarioEntity();
                u.setId(result.getInt("id"));
                u.setNome(result.getString("nome"));
                u.setUsername(result.getString("username"));
                u.setPassword(result.getString("password"));
                u.setTelefoneNum(result.getString("telefone_num"));
                u.setRua(result.getString("rua"));
                u.setCidade(result.getString("cidade"));
                u.setEstado(result.getString("estado"));

                return u;
            }
        }
        return null;
    }



}