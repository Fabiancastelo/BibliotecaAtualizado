package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexao.Conexao;
import entity.Funcionario;

public class FuncionarioDAO {

    private final LoginDAO loginDAO = new LoginDAO();

    public void criarFuncionario(Connection con, Funcionario funcionario, String usuarioLogin, String senha) throws SQLException {
        String sqlPessoa = "INSERT INTO pessoa(nome, email, telefone) VALUES (?, ?, ?)";
        String sqlFuncionario = "INSERT INTO funcionario(pessoa_id_pessoa, matricula, cargo) VALUES (?, ?, ?)";

        try (PreparedStatement psPessoa = con.prepareStatement(sqlPessoa, PreparedStatement.RETURN_GENERATED_KEYS)) {
            psPessoa.setString(1, funcionario.getNome());
            psPessoa.setString(2, funcionario.getEmail());
            psPessoa.setString(3, funcionario.getTelefone());
            psPessoa.executeUpdate();

            ResultSet rs = psPessoa.getGeneratedKeys();
            if (rs.next()) {
                int idPessoa = rs.getInt(1);

                try (PreparedStatement psFuncionario = con.prepareStatement(sqlFuncionario)) {
                    psFuncionario.setInt(1, idPessoa);
                    psFuncionario.setString(2, funcionario.getMatricula());
                    psFuncionario.setString(3, funcionario.getCargo());
                    psFuncionario.executeUpdate();
                }

                // Criar login usando mesma conexão (sem abrir nova conexão)
                loginDAO.criarLogin(con, idPessoa, usuarioLogin, senha);

            } else {
                throw new SQLException("Falha ao criar pessoa para funcionário.");
            }
        }
    }

    public void alterarFuncionario(Connection con, Funcionario funcionario) throws SQLException {
        String sqlPessoa = "UPDATE pessoa SET nome = ?, email = ?, telefone = ? WHERE id_pessoa = ?";
        String sqlFuncionario = "UPDATE funcionario SET matricula = ?, cargo = ? WHERE pessoa_id_pessoa = ?";

        try (PreparedStatement psPessoa = con.prepareStatement(sqlPessoa)) {
            psPessoa.setString(1, funcionario.getNome());
            psPessoa.setString(2, funcionario.getEmail());
            psPessoa.setString(3, funcionario.getTelefone());
            psPessoa.setInt(4, funcionario.getID());
            psPessoa.executeUpdate();
        }

        try (PreparedStatement psFuncionario = con.prepareStatement(sqlFuncionario)) {
            psFuncionario.setString(1, funcionario.getMatricula());
            psFuncionario.setString(2, funcionario.getCargo());
            psFuncionario.setInt(3, funcionario.getID());
            psFuncionario.executeUpdate();
        }
    }

    public void excluirFuncionario(Connection con, int idPessoa) throws SQLException {

        String sqlExcluirLogin = "DELETE FROM login WHERE pessoa_id_pessoa = ?";
        String sqlExcluirFuncionario = "DELETE FROM funcionario WHERE pessoa_id_pessoa = ?";
        String sqlExcluirPessoa = "DELETE FROM pessoa WHERE id_pessoa = ?";

        try (PreparedStatement psLogin = con.prepareStatement(sqlExcluirLogin)) {
            psLogin.setInt(1, idPessoa);
            psLogin.executeUpdate();
        }

        try (PreparedStatement psFuncionario = con.prepareStatement(sqlExcluirFuncionario)) {
            psFuncionario.setInt(1, idPessoa);
            psFuncionario.executeUpdate();
        }

        try (PreparedStatement psPessoa = con.prepareStatement(sqlExcluirPessoa)) {
            psPessoa.setInt(1, idPessoa);
            psPessoa.executeUpdate();
        }
    }

	    public Funcionario getById(int id) {
	        String sql = "SELECT p.id_pessoa AS ID, p.nome, p.email, p.telefone, f.cargo, f.matricula "
	        		+ "FROM funcionario f JOIN pessoa p ON f.pessoa_id_pessoa = p.id_pessoa WHERE p.id_pessoa = ?";
	        try (Connection con = Conexao.getConexao();
	        	 PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, id);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    Funcionario f = new Funcionario();
	                    f.setID(rs.getInt("ID"));
	                    f.setNome(rs.getString("nome"));
	                    f.setEmail(rs.getString("email"));
	                    f.setTelefone(rs.getString("telefone"));
	                    f.setCargo(rs.getString("cargo"));
	                    f.setMatricula(rs.getString("matricula"));
	                    return f;
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    public List<Funcionario> getAll() {
	        List<Funcionario> listaFuncionarios = new ArrayList<>();
	        String sql = "SELECT f.pessoa_id_pessoa AS ID, p.nome, p.email, p.telefone, f.cargo, f.matricula "
	        		+ "FROM funcionario f JOIN pessoa p ON f.pessoa_id_pessoa = p.id_pessoa";
	        try (Connection con = Conexao.getConexao();
	        	 PreparedStatement ps = con.prepareStatement(sql);
	        	 ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Funcionario f = new Funcionario();
	                f.setID(rs.getInt("ID"));
	                f.setNome(rs.getString("nome"));
	                f.setEmail(rs.getString("email"));
	                f.setTelefone(rs.getString("telefone"));
	                f.setCargo(rs.getString("cargo"));
	                f.setMatricula(rs.getString("matricula"));
	                listaFuncionarios.add(f);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return listaFuncionarios;
	    }
}
