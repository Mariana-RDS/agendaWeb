package com.vespertinedev.agendaWeb.model.repositories;

import com.vespertinedev.agendaWeb.model.entity.UsuarioEntity;
import com.vespertinedev.agendaWeb.model.entity.EnderecoEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;

public final class UsuarioRepository implements Repository<UsuarioEntity, Integer>{

    protected UsuarioRepository(){}

    @Override
    public void create(UsuarioEntity z) throws SQLException{

        String sql = "INSERT INTO Usuario (nome, username, password, telefone_num, id_endereco) VALUES(?,?,?,?,?)";

        try(Connection conn = ConnectionManager.getCurrentConnection();
            PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            pstm.setString(1,z.getNome());
            pstm.setString(2,z.getUsername());
            pstm.setString(3,z.getPassword());
            pstm.setString(4,z.getTelefoneNum());
            pstm.setInt(5, z.getEnderecoEntity().getId());

            pstm.executeUpdate();

            int id = 0;
            try(ResultSet generatedKeys = pstm.getGeneratedKeys()){
                if(generatedKeys.next()){
                    id = generatedKeys.getInt(1);
                }
            }
        }
    }

    @Override
    public void update(UsuarioEntity z) throws SQLException{
        String sql = "UPDATE Usuario SET nome=?, username=?, password=?, telefone_num=?, id_endereco=? WHERE id=?";

        try(Connection conn = ConnectionManager.getCurrentConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1,z.getNome());
            pstm.setString(2,z.getUsername());
            pstm.setString(3,z.getPassword());
            pstm.setString(4,z.getTelefoneNum());
            pstm.setInt(5,z.getEnderecoEntity().getId());
            pstm.setInt(6,z.getId());

            pstm.executeUpdate();

            EnderecoEntity endereco = z.getEnderecoEntity();
            String sqlEndereco = "UPDATE Endereco SET rua = ?, cidade = ?, estado = ? WHERE id = ?";

            try (PreparedStatement pstmEndereco = conn.prepareStatement(sqlEndereco)) {
                pstmEndereco.setString(1, endereco.getRua());
                pstmEndereco.setString(2, endereco.getCidade());
                pstmEndereco.setString(3, endereco.getEstado());
                pstmEndereco.setInt(4, endereco.getId());
                pstmEndereco.executeUpdate();
            }
        }
    }

    @Override
    public UsuarioEntity read(Integer k) throws SQLException{
        String sql = "SELECT * FROM Usuario WHERE id = ?";
        try(Connection conn = ConnectionManager.getCurrentConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1,k);
            ResultSet result = pstm.executeQuery();

            UsuarioEntity u = null;

            if(result.next()){
                u = new UsuarioEntity();
                u.setId(result.getInt("id"));
                u.setNome(result.getString("nome"));
                u.setUsername(result.getString("username"));
                u.setPassword(result.getString("password"));
                u.setTelefoneNum(result.getString("telefone_num"));

                Integer enderecoId = result.getInt("id_endereco");
                EnderecoEntity endereco = new EnderecoEntity();
                endereco.setId(enderecoId);
                u.setEnderecoEntity(endereco);
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
}
