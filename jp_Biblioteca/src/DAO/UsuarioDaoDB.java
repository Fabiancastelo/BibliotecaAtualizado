package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexao.Conexao;
import entity.Usuario;

public class UsuarioDaoDB {

    private final LoginDAO loginDAO = new LoginDAO();

    public boolean criarUsuario(Usuario usuario, String usuarioLogin, String senha) throws SQLException {
        String sqlPessoa = "INSERT INTO pessoa(nome, email, telefone) VALUES (?, ?, ?)";
        String sqlUsuario = "INSERT INTO usuario(pessoa_id_pessoa) VALUES (?)";

        try (Connection con = Conexao.getConexao()) {
            con.setAutoCommit(false);

            try (PreparedStatement psPessoa = con.prepareStatement(sqlPessoa, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psPessoa.setString(1, usuario.getNome());
                psPessoa.setString(2, usuario.getEmail());
                psPessoa.setString(3, usuario.getTelefone());
                psPessoa.executeUpdate();

                ResultSet rs = psPessoa.getGeneratedKeys();
                if (rs.next()) {
                    int idPessoa = rs.getInt(1);

                    try (PreparedStatement psUsuario = con.prepareStatement(sqlUsuario)) {
                        psUsuario.setInt(1, idPessoa);
                        psUsuario.executeUpdate();
                    }

                    loginDAO.criarLogin(con, idPessoa, usuarioLogin, senha);
                } else {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;
        }
    }

    public boolean alterarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE pessoa SET nome = ?, email = ?, telefone = ? WHERE id_pessoa = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefone());
            ps.setInt(4, usuario.getID());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluirUsuario(int idPessoa) throws SQLException {
        try (Connection con = Conexao.getConexao()) {
            con.setAutoCommit(false);

            loginDAO.excluirLogin(con, idPessoa);

            try (PreparedStatement psUsuario = con.prepareStatement("DELETE FROM usuario WHERE pessoa_id_pessoa = ?")) {
                psUsuario.setInt(1, idPessoa);
                psUsuario.executeUpdate();
            }

            try (PreparedStatement psPessoa = con.prepareStatement("DELETE FROM pessoa WHERE id_pessoa = ?")) {
                psPessoa.setInt(1, idPessoa);
                psPessoa.executeUpdate();
            }

            con.commit();
            return true;
        }
    }

    public List<Usuario> getAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.pessoa_id_pessoa as ID, p.nome, p.email, p.telefone FROM usuario u JOIN pessoa p ON u.pessoa_id_pessoa = p.id_pessoa";

        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setID(rs.getInt("ID"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setTelefone(rs.getString("telefone"));
                usuarios.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuários: " + e.getMessage());
        }

        return usuarios;
    }

    public Usuario getById(int id) {
        String sql = "SELECT p.id_pessoa AS ID, p.nome, p.email, p.telefone FROM usuario u JOIN pessoa p ON u.pessoa_id_pessoa = p.id_pessoa WHERE p.id_pessoa = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setID(rs.getInt("ID"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setTelefone(rs.getString("telefone"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }

        return null;
    }
} 
