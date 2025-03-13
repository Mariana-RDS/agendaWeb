package com.vespertinedev.agendaWeb.model.repositories;

import com.vespertinedev.agendaWeb.model.entity.ContatoEntity;
import com.vespertinedev.agendaWeb.model.entity.TelefoneEntity;
import com.vespertinedev.agendaWeb.model.entity.UsuarioEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public final class ContatoRepository implements GenericRepository<ContatoEntity, Integer> {

    public ContatoRepository() {}

    @Override
    public void create(ContatoEntity contato) throws SQLException {
        throw new UnsupportedOperationException("");
    }

    public void create(ContatoEntity z, Integer usuarioId) throws SQLException {
        String sqlContato = "INSERT INTO Contato (nome, email, id_usuario, rua, cidade, estado) VALUES(?,?,?,?,?,?)";

        try (Connection conn = ConnectionManager.getCurrentConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstm = conn.prepareStatement(sqlContato, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setString(1, z.getNome());
                pstm.setString(2, z.getEmail());
                pstm.setInt(3, usuarioId);
                pstm.setString(4, z.getRua());
                pstm.setString(5, z.getCidade());
                pstm.setString(6, z.getEstado());
                pstm.executeUpdate();

                try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        z.setId(generatedKeys.getInt(1));
                    }
                }
            }

            if (z.getTelefones() != null && !z.getTelefones().isEmpty()) {
                String sqlTelefone = "INSERT INTO Telefone(numero, id_contato) VALUES(?,?)";
                try (PreparedStatement pstmTelefone = conn.prepareStatement(sqlTelefone)) {
                    for (TelefoneEntity telefone : z.getTelefones()) {
                        pstmTelefone.setString(1, telefone.getNumero());
                        pstmTelefone.setInt(2, z.getId());
                        pstmTelefone.addBatch();
                    }
                    pstmTelefone.executeBatch();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void update(ContatoEntity contato) throws SQLException {
        throw new UnsupportedOperationException("");
    }

    public void update(ContatoEntity z, Integer usuarioId) throws SQLException {
        String sqlContato = "UPDATE Contato SET nome=?, email=?, id_usuario=?, rua=?, cidade=?, estado=? WHERE id=?";
        Connection conn = null;

        try {
            conn = ConnectionManager.getCurrentConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmContato = conn.prepareStatement(sqlContato)) {

                pstmContato.setString(1, z.getNome());
                pstmContato.setString(2, z.getEmail());
                pstmContato.setInt(3, usuarioId);
                pstmContato.setString(4, z.getRua());
                pstmContato.setString(5, z.getCidade());
                pstmContato.setString(6, z.getEstado());
                pstmContato.setInt(7, z.getId());
                pstmContato.executeUpdate();
            }

            if (z.getTelefones() != null && !z.getTelefones().isEmpty()) {
                String sqlTelefone = "UPDATE Telefone SET numero=? WHERE id=? AND id_contato=?";
                try (PreparedStatement pstmTelefone = conn.prepareStatement(sqlTelefone)) {
                    for (TelefoneEntity telefone : z.getTelefones()) {
                        if (telefone.getId() == null) {
                            throw new SQLException("ID do telefone não pode ser nulo");
                        }
                        pstmTelefone.setString(1, telefone.getNumero());
                        pstmTelefone.setInt(2, telefone.getId());
                        pstmTelefone.setInt(3, z.getId());
                        pstmTelefone.addBatch();
                    }
                    pstmTelefone.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                conn.close();
            }
        }
    }

    @Override
    public ContatoEntity read(Integer k) throws SQLException {
        String sql = "SELECT * FROM Contato WHERE id = ?";
        try (Connection conn = ConnectionManager.getCurrentConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, k);
            ResultSet result = pstm.executeQuery();

            if (result.next()) {
                ContatoEntity c = new ContatoEntity();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setEmail(result.getString("email"));
                c.setRua(result.getString("rua"));
                c.setCidade(result.getString("cidade"));
                c.setEstado(result.getString("estado"));

                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setId(result.getInt("id_usuario"));
                c.setUsuarioEntity(usuario);

                List<TelefoneEntity> telefones = new ArrayList<>();
                String sqlTelefone = "SELECT * FROM Telefone WHERE id_contato = ?";
                try (PreparedStatement pstmTelefone = conn.prepareStatement(sqlTelefone)) {
                    pstmTelefone.setInt(1, k);
                    ResultSet telefoneResult = pstmTelefone.executeQuery();

                    while (telefoneResult.next()) {
                        TelefoneEntity telefone = new TelefoneEntity();
                        telefone.setId(telefoneResult.getInt("id"));
                        telefone.setNumero(telefoneResult.getString("numero"));
                        telefones.add(telefone);
                    }
                }
                c.setTelefoneEntity(telefones);
                return c;
            }
            return null;
        }
    }

    @Override
    public void delete(Integer k) throws SQLException {
        String sql = "DELETE FROM Contato WHERE id = ?";
        try (Connection conn = ConnectionManager.getCurrentConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, k);
            pstm.executeUpdate();
        }
    }

    public List<ContatoEntity> readAll(Integer usuarioId) throws SQLException {
        String sql = "SELECT * FROM Contato WHERE id_usuario = ?";
        List<ContatoEntity> contatos = new ArrayList<>();

        try (Connection conn = ConnectionManager.getCurrentConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, usuarioId);
            ResultSet result = pstm.executeQuery();

            while (result.next()) {
                ContatoEntity c = new ContatoEntity();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setEmail(result.getString("email"));
                c.setRua(result.getString("rua"));
                c.setCidade(result.getString("cidade"));
                c.setEstado(result.getString("estado"));

                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setId(result.getInt("id_usuario"));
                c.setUsuarioEntity(usuario);

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
        }
        return contatos;
    }

    public List<ContatoEntity> findByUsuarioId(Integer usuarioId) throws SQLException {
        return readAll(usuarioId);
    }

    public List<ContatoEntity> findByNomeUsuario(String nome, Integer usuarioId) throws SQLException {
        String sql = "SELECT * FROM Contato WHERE nome LIKE ? AND id_usuario = ?";
        List<ContatoEntity> contatos = new ArrayList<>();

        try (Connection conn = ConnectionManager.getCurrentConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, "%" + nome + "%");
            pstm.setInt(2, usuarioId);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                ContatoEntity contato = new ContatoEntity();
                contato.setId(rs.getInt("id"));
                contato.setNome(rs.getString("nome"));
                contato.setEmail(rs.getString("email"));
                contato.setRua(rs.getString("rua"));
                contato.setCidade(rs.getString("cidade"));
                contato.setEstado(rs.getString("estado"));
                contatos.add(contato);
            }
        }
        return contatos;
    }
}
