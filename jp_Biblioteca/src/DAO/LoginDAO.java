package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexao.Conexao;
import entity.PessoaLogin;

public class LoginDAO {

	public boolean isGerenteOuADM(int pessoaId) {
		String sql = "SELECT cargo FROM funcionario WHERE pessoa_id_pessoa = ?";

		try (Connection conn = conexao.Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, pessoaId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String cargo = rs.getString("cargo");
					return cargo != null && (cargo.equalsIgnoreCase("ADM") || cargo.equalsIgnoreCase("Gerente"));
				}
			}

		} catch (Exception e) {
			System.out.println("Erro ao verificar cargo: " + e.getMessage());
		}

		return false;
	}

	public boolean isFuncionario(int pessoaId) {
		String sql = "SELECT 1 FROM funcionario WHERE pessoa_id_pessoa = ?";
		try (Connection conn = conexao.Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, pessoaId);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		} catch (Exception e) {
			System.out.println("Erro ao verificar se é funcionário: " + e.getMessage());
		}
		return false;
	}

	public void criarLogin(Connection conn, int pessoaId, String usuario, String senha) throws SQLException {
		String sql = "INSERT INTO login(pessoa_id_pessoa, usuario, senha) VALUES (?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, pessoaId);
			stmt.setString(2, usuario);
			stmt.setString(3, senha);
			stmt.executeUpdate();
		}
	}

	public void alterarLogin(Connection conn, int pessoaId, String novoUsuario, String novaSenha) throws SQLException {
		String sql = "UPDATE login SET usuario = ?, senha = ? WHERE pessoa_id_pessoa = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, novoUsuario);
			stmt.setString(2, novaSenha);
			stmt.setInt(3, pessoaId);
			stmt.executeUpdate();
		}
	}

	public void excluirLogin(Connection conn, int pessoaId) throws SQLException {
		String sql = "DELETE FROM login WHERE pessoa_id_pessoa = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, pessoaId);
			stmt.executeUpdate();
		}
	}

	public boolean loginExiste(int pessoaId) {
		String sql = "SELECT 1 FROM login WHERE pessoa_id_pessoa = ?";
		try (Connection conn = conexao.Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, pessoaId);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			System.out.println("Erro ao verificar se login existe: " + e.getMessage());
		}
		return false;
	}

	public PessoaLogin autenticar(String usuario, String senha) {
		String sql = """
				    SELECT p.id_pessoa, p.nome, l.usuario, f.cargo
				    FROM login l
				    JOIN pessoa p ON p.id_pessoa = l.pessoa_id_pessoa
				    LEFT JOIN funcionario f ON f.pessoa_id_pessoa = p.id_pessoa
				    WHERE l.usuario = ? AND l.senha = ?
				""";

		try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, usuario);
			stmt.setString(2, senha);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int idPessoa = rs.getInt("id_pessoa");
					String nome = rs.getString("nome");
					String usuarioEncontrado = rs.getString("usuario");
					String cargo = rs.getString("cargo"); // pode ser null se não for funcionário

					return new PessoaLogin(idPessoa, nome, usuarioEncontrado, cargo);
				}
			}

		} catch (SQLException e) {
			System.out.println("Erro ao autenticar: " + e.getMessage());
		}

		return null;
	}

}