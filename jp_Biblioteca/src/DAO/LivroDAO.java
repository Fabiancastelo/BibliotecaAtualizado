package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import books.Livro;
import conexao.Conexao;

public class LivroDAO {

	public boolean cadastrarLivro(Livro livros) {
		String sql = "INSERT INTO livro(titulo, autor, isbn, anoPublicacao)VALUES(?,?,?,?);";
		try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, livros.getTitulo());
			stmt.setString(2, livros.getAutor());
			stmt.setString(3, livros.getIsbn());
			stmt.setDate(4, Date.valueOf(livros.getAnoPublicacao()));

			// retorno das linhas afetadas
			return stmt.executeUpdate() > 0;

		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Erro: ISBN já cadastrado.");
		} catch (Exception e) {
			System.out.println("Erro ao cadastrar livro: " + e.getMessage());
		}
		return false;
	}

	public List<Livro> getAll() {
		List<Livro> livros = new ArrayList<>();
		String sql = "SELECT * FROM livro";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql);
				ResultSet rs = stm.executeQuery()) {

			while (rs.next()) {
				Livro l = new Livro();
				l.setID(rs.getInt("id_livro"));
				l.setTitulo(rs.getString("titulo"));
				l.setAutor(rs.getString("autor"));
				Date dataSql = rs.getDate("anoPublicacao");
				if (dataSql != null) {
					l.setAnoPublicacao(dataSql.toLocalDate());
				}
				l.setIsbn(rs.getString("isbn"));
				livros.add(l);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar livros: " + e.getMessage());
		}
		return livros;

	}

	public Livro buscarLivro(String isbn) {
		String sql = "SELECT * FROM livro WHERE isbn = ?";
		Livro livro = null;

		try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, isbn);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				livro = new Livro();
				livro.setID(rs.getInt("id_livro"));
				livro.setTitulo(rs.getString("titulo"));
				livro.setAutor(rs.getString("autor"));
				Date dataSql = rs.getDate("anoPublicacao");
				if (dataSql != null) {
					livro.setAnoPublicacao(dataSql.toLocalDate());
				}
				livro.setIsbn(rs.getString("isbn"));
			}

		} catch (Exception e) {
			System.out.println("Erro ao buscar livro: " + e.getMessage());
		}
		return livro;
	}

	public boolean alterarLivro(Livro livros) {
		String sql = "UPDATE livro SET titulo = ?, autor = ?, anoPublicacao = ? WHERE isbn = ?;";

		try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, livros.getTitulo());
			stmt.setString(2, livros.getAutor());
			stmt.setDate(3, Date.valueOf(livros.getAnoPublicacao()));
			stmt.setString(4, livros.getIsbn());

			// retorno das linhas afetadas
			return stmt.executeUpdate() > 0;

		} catch (Exception e) {
			System.out.println("Erro ao alterar livro: " + e.getMessage());
		}
		return false;
	}

	public boolean excluirLivro(String isbn) {
		String sql = "DELETE FROM livro WHERE isbn = ?;";
		try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, isbn);
			return stmt.executeUpdate() > 0;
		} catch (Exception e) {
			System.out.println("Erro ao excluir livro: " + e.getMessage());
		}
		return false;
	}
	
	public Livro getById(int id) {
        String sql = "SELECT * FROM livro WHERE id_livro = ?";
        try (Connection con = Conexao.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Livro l = new Livro();
                l.setID(rs.getInt("id_livro"));
                l.setTitulo(rs.getString("titulo"));
                l.setAutor(rs.getString("autor"));
                Date dataSql = rs.getDate("anoPublicacao");
                if (dataSql != null) {
                    l.setAnoPublicacao(dataSql.toLocalDate());
                }
                l.setIsbn(rs.getString("isbn"));
                return l;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public Livro getByISBN(String isbn) {
	    String sql = "SELECT * FROM livro WHERE isbn = ?";
	    try (Connection con = Conexao.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
	        stm.setString(1, isbn);
	        ResultSet rs = stm.executeQuery();
	        if (rs.next()) {
	            Livro l = new Livro();
	            l.setID(rs.getInt("id_livro"));
	            l.setTitulo(rs.getString("titulo"));
	            l.setAutor(rs.getString("autor"));
	            Date dataSql = rs.getDate("anoPublicacao");
	            if (dataSql != null) {
	                l.setAnoPublicacao(dataSql.toLocalDate());
	            }
	            l.setIsbn(rs.getString("isbn"));
	            return l;
	        }
	    } catch (SQLException e) {
	        System.out.println("Erro ao buscar livro por ISBN: " + e.getMessage());
	    }
	    return null;
	}

}
