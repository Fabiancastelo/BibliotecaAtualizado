package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import books.Emprestimo;
import books.Livro;
import conexao.Conexao;
import entity.Usuario;

public class EmprestimoDAO {
	public void emprestar(Emprestimo emprestimo) {
		if (emprestimo.getLivros() == null || emprestimo.getLivros().isEmpty()) {
			System.out.println("Empréstimo não pode ser registrado: nenhum livro foi informado.");
			return;
		}

		String sqlEmp = "INSERT INTO emprestimo(data_emp, usuario_id_usuario) VALUES(?, ?)";
		try (Connection con = Conexao.getConexao();
				PreparedStatement stmEmp = con.prepareStatement(sqlEmp, PreparedStatement.RETURN_GENERATED_KEYS)) {

			stmEmp.setDate(1, Date.valueOf(emprestimo.getDataEmp()));
			stmEmp.setInt(2, emprestimo.getUsuario().getID());
			stmEmp.executeUpdate();

			try (ResultSet rs = stmEmp.getGeneratedKeys()) {
				if (rs.next()) {
					emprestimo.setId(rs.getInt(1));
				}
			}

			String sqlLivroEmp = "INSERT INTO livro_emprestimo(isbn, id_emprestimo, data_prevista) VALUES(?, ?, ?)";
			try (PreparedStatement stmLivro = con.prepareStatement(sqlLivroEmp)) {
				for (Livro livro : emprestimo.getLivros()) {
					stmLivro.setString(1, livro.getIsbn());
					stmLivro.setInt(2, emprestimo.getId());
					stmLivro.setDate(3, Date.valueOf(emprestimo.getDataPrev()));
					stmLivro.addBatch();
				}
				stmLivro.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void devolver(Emprestimo emprestimo) {
		String sql = "UPDATE livro_emprestimo SET data_devolucao = ? WHERE id_livro = ? AND id_emprestimo = ?";
		try (Connection con = Conexao.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
			for (Livro livro : emprestimo.getLivros()) {
				stm.setDate(1, Date.valueOf(LocalDate.now()));
				stm.setInt(2, livro.getID());
				stm.setInt(3, emprestimo.getId());
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Emprestimo getById(int id) {
		Emprestimo emp = null;
		String sql = "SELECT * FROM emprestimo WHERE id_emprestimo = ?";
		try (Connection con = Conexao.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {

			stm.setInt(1, id);
			try (ResultSet rs = stm.executeQuery()) {
				if (rs.next()) {
					emp = new Emprestimo();
					emp.setId(rs.getInt("id_emprestimo"));
					emp.setDataEmp(rs.getDate("data_emp").toLocalDate());
					emp.setUsuario(new UsuarioDaoDB().getById(rs.getInt("usuario_id_usuario")));
				}
			}

			if (emp != null) {
				String sql2 = "SELECT * FROM livro_emprestimo WHERE id_emprestimo = ?";
				try (PreparedStatement stm2 = con.prepareStatement(sql2)) {
					stm2.setInt(1, emp.getId());
					try (ResultSet rs2 = stm2.executeQuery()) {
						List<Livro> lista = new ArrayList<>();
						LocalDate prevista = null;
						LocalDate devolucao = null;
						while (rs2.next()) {
							lista.add(new LivroDAO().getById(rs2.getInt("id_livro")));
							if (prevista == null)
								prevista = rs2.getDate("data_prevista").toLocalDate();
							if (rs2.getDate("data_devolucao") != null)
								devolucao = rs2.getDate("data_devolucao").toLocalDate();
						}
						emp.setLivros(lista);
						emp.setDataPrev(prevista);
						emp.setDataDev(devolucao);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return emp;
	}

	public List<Emprestimo> listarEmprestimos() {
	    List<Emprestimo> lista = new ArrayList<>();
	    String sql = """
	        SELECT e.id_emprestimo, e.data_emp, u.id_usuario, l.usuario 
	        FROM emprestimo e
	        JOIN usuario u ON e.usuario_id_usuario = u.id_usuario
	        JOIN login l ON l.pessoa_id_pessoa = u.pessoa_id_pessoa
	    """;

	    try (Connection conexao = Conexao.getConexao();
	         PreparedStatement stmt = conexao.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Emprestimo emp = new Emprestimo();
	            emp.setId(rs.getInt("id_emprestimo"));
	            emp.setDataEmp(rs.getDate("data_emp").toLocalDate());

	            Usuario usuario = new Usuario();
	            usuario.setID(rs.getInt("id_usuario"));
	            usuario.setNome(rs.getString("usuario"));
	            emp.setUsuario(usuario);

	         
	            String sqlLivros = "SELECT * FROM livro_emprestimo WHERE id_emprestimo = ?";
	            try (PreparedStatement stmtLivros = conexao.prepareStatement(sqlLivros)) {
	                stmtLivros.setInt(1, emp.getId());
	                try (ResultSet rsLivros = stmtLivros.executeQuery()) {
	                    List<Livro> livros = new ArrayList<>();
	                    LocalDate dataPrev = null;
	                    LocalDate dataDev = null;
	                    while (rsLivros.next()) {
	                        int idLivro = rsLivros.getInt("id_livro");
	                        Livro livro = new LivroDAO().getById(idLivro);
	                        livros.add(livro);

	                        if (dataPrev == null && rsLivros.getDate("data_prevista") != null) {
	                            dataPrev = rsLivros.getDate("data_prevista").toLocalDate();
	                        }

	                        if (rsLivros.getDate("data_devolucao") != null) {
	                            dataDev = rsLivros.getDate("data_devolucao").toLocalDate();
	                        }
	                    }
	                    emp.setLivros(livros);
	                    emp.setDataPrev(dataPrev);
	                    emp.setDataDev(dataDev);
	                }
	            }

	            lista.add(emp);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lista;
	}
	
	public boolean livroSemDevolucao(int idLivro) {
	    String sql = "SELECT COUNT(*) FROM livro_emprestimo WHERE id_livro = ? AND data_devolucao IS NULL";
	    try (Connection con = Conexao.getConexao();
	         PreparedStatement stm = con.prepareStatement(sql)) {
	        stm.setInt(1, idLivro);
	        try (ResultSet rs = stm.executeQuery()) {
	            if (rs.next()) {
	                int count = rs.getInt(1);
	                return count > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public int contarEmprestimosAtivo(int idUsuario) {
	    String sql = """
	        SELECT COUNT(*) FROM livro_emprestimo le
	        JOIN emprestimo e ON le.id_emprestimo = e.id_emprestimo
	        WHERE e.usuario_id_usuario = ? AND le.data_devolucao IS NULL
	    """;

	    try (Connection con = Conexao.getConexao();
	         PreparedStatement stm = con.prepareStatement(sql)) {
	        stm.setInt(1, idUsuario);
	        try (ResultSet rs = stm.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}
	
	public List<Emprestimo> getEmprestimosPorUsuario(int idUsuario) {
	    List<Emprestimo> lista = new ArrayList<>();

	    String sql = """
	        SELECT e.id_emprestimo, e.data_emp
	        FROM emprestimo e
	        WHERE e.usuario_id_usuario = ?
	    """;

	    try (Connection con = Conexao.getConexao();
	         PreparedStatement stm = con.prepareStatement(sql)) {

	        stm.setInt(1, idUsuario);
	        try (ResultSet rs = stm.executeQuery()) {
	            while (rs.next()) {
	                Emprestimo emp = new Emprestimo();
	                emp.setId(rs.getInt("id_emprestimo"));
	                emp.setDataEmp(rs.getDate("data_emp").toLocalDate());
	                emp.setUsuario(new UsuarioDaoDB().getById(idUsuario));

	                List<Livro> livros = new ArrayList<>();
	                String sqlLivros = "SELECT * FROM livro_emprestimo WHERE id_emprestimo = ?";
	                try (PreparedStatement stmLivros = con.prepareStatement(sqlLivros)) {
	                    stmLivros.setInt(1, emp.getId());
	                    try (ResultSet rsLivros = stmLivros.executeQuery()) {
	                        LocalDate dataPrev = null;
	                        LocalDate dataDev = null;
	                        while (rsLivros.next()) {
	                            Livro livro = new LivroDAO().getById(rsLivros.getInt("id_livro"));
	                            livros.add(livro);

	                            if (dataPrev == null && rsLivros.getDate("data_prevista") != null) {
	                                dataPrev = rsLivros.getDate("data_prevista").toLocalDate();
	                            }
	                            if (rsLivros.getDate("data_devolucao") != null) {
	                                dataDev = rsLivros.getDate("data_devolucao").toLocalDate();
	                            }
	                        }
	                        emp.setLivros(livros);
	                        emp.setDataPrev(dataPrev);
	                        emp.setDataDev(dataDev);
	                    }
	                }
	                lista.add(emp);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lista;
	}

}