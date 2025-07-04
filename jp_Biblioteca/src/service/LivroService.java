package service;

import java.time.LocalDate;
import java.util.List;

import DAO.EmprestimoDAO;
import DAO.LivroDAO;
import DAO.LoginDAO;
import books.Livro;
import entity.PessoaLogin;

public class LivroService {
	private final LivroDAO dao = new LivroDAO();
	private final LoginDAO loginDAO = new LoginDAO();

	public void cadastrarLivro(String titulo, String autor, String isbn, LocalDate data) {
		Livro livro = new Livro();
		livro.setTitulo(titulo);
		livro.setAutor(autor);
		livro.setIsbn(isbn);
		livro.setAnoPublicacao(data);

		boolean sucesso = dao.cadastrarLivro(livro);

		System.out.println(sucesso ? "livro adicionado!" : "Erro ao adicionar livro.");
	}

	public void alterarLivro(String isbn, String novoTitulo, String novoAutor, LocalDate novaData) {
		Livro livro = dao.buscarLivro(isbn);
		if (livro == null) {
			System.out.println("Livro não encontrado.");
			return;
		}

		livro.setTitulo(novoTitulo);
		livro.setAutor(novoAutor);
		livro.setAnoPublicacao(novaData);

		boolean sucesso = dao.alterarLivro(livro);

		System.out.println(sucesso ? "Livro atualizado!" : "Erro ao atualizar livro.");
	}

	public void excluirLivro(String isbn) {
		boolean sucesso = dao.excluirLivro(isbn);

		System.out.println(sucesso ? "Livro exclído com sucesso!" : "Erro ao excluir livro.");
	}

	public void buscarLivro(String isbn) {
		Livro livro = dao.buscarLivro(isbn);
		if (livro != null) {
			System.out.println(livro); // puxando pelo ToString
		} else {
			System.out.println("Livro não encontrado");
		}
	}

	public void listarLivros() {
	    LivroDAO dao = new LivroDAO();
	    EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

	    List<Livro> livros = dao.getAll();

	    if (livros.isEmpty()) {
	        System.out.println("Nenhum livro cadastrado.");
	        return;
	    }

	    System.out.println("\n=== Lista de Livros ===");
	    for (Livro livro : livros) {
	        boolean emprestado = emprestimoDAO.livroSemDevolucao(livro.getID());
	        String status = emprestado ? " (Indisponível)" : " (Disponível)";
	        System.out.println(livro.toString() + status);
	    }
	}

	public PessoaLogin autenticar(String usuario, String senha) {
		return loginDAO.autenticar(usuario, senha);
	}

}
