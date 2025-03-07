package com.vespertinedev.agendaWeb.model.repositories;

import com.vespertinedev.agendaWeb.model.entity.ContatoEntity;
import com.vespertinedev.agendaWeb.model.entity.EnderecoEntity;
import com.vespertinedev.agendaWeb.model.entity.TelefoneEntity;
import com.vespertinedev.agendaWeb.model.entity.UsuarioEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public final class ContatoRepository implements Repository<ContatoEntity, Integer>{

    protected  ContatoRepository(){}

    @Override
    public void create(ContatoEntity z) throws SQLException {

        String sql = "INSERT INTO Contato (nome, email, id_usuario, id_endereco) VALUES(?,?,?,?)";

        try(Connection conn = ConnectionManager.getCurrentConnection();
            PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            pstm.setString(1,z.getNome());
            pstm.setString(2,z.getEmail());
            pstm.setInt(3,z.getUsuarioEntity().getId());
            pstm.setInt(4,z.getEnderecoEntity().getId());

            pstm.executeUpdate();

            int id = 0;
            try(ResultSet generatedKeys = pstm.getGeneratedKeys()){
                if(generatedKeys.next()){
                    id = generatedKeys.getInt(1);
                }
            }

            if(z.getTelefones() != null && !z.getTelefones().isEmpty()){
                sql = "INSERT INTO Telefone(numero, id_contato) VALUES(?,?)";
                try(PreparedStatement pstmTelefone =conn.prepareStatement(sql)){
                    for(TelefoneEntity telefone : z.getTelefones()){
                        pstmTelefone.setString(1,telefone.getNumero());
                        pstmTelefone.setInt(2, id);
                        pstmTelefone.addBatch();
                    }
                    pstmTelefone.executeBatch();
                }
            }
        }
    }

    @Override
    public void update(ContatoEntity z) throws SQLException{
        String sql = "UPDATE Contato SET  nome=?, email=?, id_usuario=?, id_endereco=? WHERE id=?";

        try(Connection conn = ConnectionManager.getCurrentConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1,z.getNome());
            pstm.setString(2,z.getEmail());
            pstm.setInt(3,z.getUsuarioEntity().getId());
            pstm.setInt(4,z.getEnderecoEntity().getId());
            pstm.setInt(5,z.getId());

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

            if(z.getTelefones() != null && !z.getTelefones().isEmpty()){
                sql = "UPDATE Telefone SET numero=? WHERE id=?";
                try(PreparedStatement pstmTelefone = conn.prepareStatement(sql)){
                    for(TelefoneEntity telefone : z.getTelefones()){
                        pstmTelefone.setString(1,telefone.getNumero());
                        pstmTelefone.setInt(2,telefone.getId());
                        pstmTelefone.addBatch();
                    }
                    pstmTelefone.executeBatch();
                }
            }
        }
    }

    @Override
    public ContatoEntity read(Integer k) throws SQLException{
        String sql = "SELECT * FROM Contato WHERE id = ?";
        try(Connection conn = ConnectionManager.getCurrentConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, k);
            ResultSet result = pstm.executeQuery();

            ContatoEntity c = null;

            if (result.next()) {
                c = new ContatoEntity();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setEmail(result.getString("email"));

                Integer usuarioId = result.getInt("id_usuario");
                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setId(usuarioId);
                c.setUsuarioEntity(usuario);

                Integer enderecoId = result.getInt("id_endereco");
                EnderecoEntity endereco = new EnderecoEntity();
                endereco.setId(enderecoId);
                c.setEnderecoEntity(endereco);

                List<TelefoneEntity> telefones = new ArrayList<>();
                String sqlTelefone = "SELECT * FROM Telefone WHERE id_contato = ?";
                try (PreparedStatement pstmTelefone = conn.prepareStatement(sqlTelefone)) {
                    pstmTelefone.setInt(1, k);
                    ResultSet telefoneResult = pstmTelefone.executeQuery();

                    while (telefoneResult.next()) {
                        TelefoneEntity telefone = new TelefoneEntity();
                        telefone.setId(telefoneResult.getInt("id"));
                        telefone.setNumero(telefoneResult.getString("numero"));

                        ContatoEntity contato = new ContatoEntity();
                        contato.setId(k);
                        telefone.setContatoEntity(contato);
                        telefones.add(telefone);
                    }
                }
                c.setTelefoneEntity(telefones);
            }
            return c;
        }
    }
    
    public void delete(Integer k) throws SQLException{
        String sql = "DELETE FROM Contato WHERE id = ?";

        try(Connection conn =ConnectionManager.getCurrentConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setInt(1,k);
            pstm.executeUpdate();
        }
    }

    public List<ContatoEntity> readAll() throws SQLException{
        String sql = "SELECT * FROM Contato";
        try(Connection conn = ConnectionManager.getCurrentConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)){
            ResultSet result = pstm.executeQuery();

            List<ContatoEntity> contatos = new ArrayList<ContatoEntity>();

            while(result.next()){

                ContatoEntity c = new ContatoEntity();

                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setEmail(result.getString("email"));

                Integer usuarioId = result.getInt("id_usuario");
                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setId(usuarioId);
                c.setUsuarioEntity(usuario);

                Integer enderecoId = result.getInt("id_endereco");
                EnderecoEntity endereco = new EnderecoEntity();
                endereco.setId(enderecoId);
                c.setEnderecoEntity(endereco);

                List<TelefoneEntity> telefones = new ArrayList<>();
                String sqlTelefone = "SELECT * FROM Telefone WHERE id_contato = ?";
                try (PreparedStatement pstmTelefone = conn.prepareStatement(sqlTelefone)) {
                    pstmTelefone.setInt(1, c.getId());
                    ResultSet telefoneResult = pstmTelefone.executeQuery();

                    while (telefoneResult.next()) {
                        TelefoneEntity telefone = new TelefoneEntity();
                        telefone.setId(telefoneResult.getInt("id"));
                        telefone.setNumero(telefoneResult.getString("numero"));
                        telefones.add(telefone);
                    }
                }
                c.setTelefoneEntity(telefones);
                contatos.add(c);
            }
            return contatos;
        }
    }
}
