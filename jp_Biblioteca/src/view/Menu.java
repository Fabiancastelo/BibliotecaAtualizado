package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import DAO.EmprestimoDAO;
import DAO.FuncionarioDAO;
import DAO.LivroDAO;
import DAO.LoginDAO;
import DAO.UsuarioDaoDB;
import books.Emprestimo;
import books.Livro;
import entity.Funcionario;
import entity.PessoaLogin;
import entity.Usuario;
import service.EmprestimoService;
import service.LivroService;
import service.PessoaService;

public class Menu {
	private final LivroService livroService = new LivroService();
	private final PessoaService pessoaService = new PessoaService();
	private final EmprestimoService emprestimoService = new EmprestimoService();

	private final Scanner sc = new Scanner(System.in);
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private final LoginDAO loginDAO = new LoginDAO();
	private final UsuarioDaoDB usuarioDao = new UsuarioDaoDB();
	private final FuncionarioDAO funcionarioDao = new FuncionarioDAO();

	public void iniciarSistema() {
		System.out.println("=== Login do Sistema ===");
		System.out.print("Usuário: ");
		String usuario = sc.nextLine();

		System.out.print("Senha: ");
		String senha = sc.nextLine();

		PessoaLogin pessoaLogada = loginDAO.autenticar(usuario, senha);

		if (pessoaLogada != null) {
			System.out.println("Bem-vindo, " + pessoaLogada.getNome() + " (" + pessoaLogada.getCargo() + ")");
			mostrarMenu(pessoaLogada);
		} else {
			System.out.println("Usuário ou senha inválidos.");
		}
	}

	public void mostrarMenu(PessoaLogin usuario) {
		boolean funcionario = loginDAO.isFuncionario(usuario.getIdPessoa());
		boolean gerenteOuADM = loginDAO.isGerenteOuADM(usuario.getIdPessoa());

		while (true) {
			System.out.println("\n===== MENU =====");

			if (funcionario) {
				System.out.println("1 - Adicionar livro");
				System.out.println("2 - Alterar livro");
				System.out.println("3 - Excluir livro");
				System.out.println("4 - Registrar Empréstimo");
				System.out.println("5 - Devolver Livro");
				System.out.println("6 - Listar Empréstimos (todos)");

				System.out.println("7 - Criar usuário");
				System.out.println("8 - Alterar usuário");
				System.out.println("9 - Excluir usuário");

				System.out.println("10 - Criar funcionário");
				System.out.println("11 - Alterar funcionário");
				System.out.println("12 - Excluir funcionário");

				System.out.println("0 - Sair");
			} else {
				System.out.println("1 - Buscar livro");
				System.out.println("2 - Listar todos os livros");
				System.out.println("3 - Meus Empréstimos");
				System.out.println("4 - Sair");
			}

			System.out.print("Escolha uma opção: ");
			int opcao = sc.nextInt();
			sc.nextLine(); // consumir quebra de linha

			if (funcionario) {
				switch (opcao) {
				case 1 -> cadastrarLivro();
				case 2 -> alterarLivro();
				case 3 -> excluirLivro();
				case 4 -> registrarEmprestimo(usuario);
				case 5 -> devolverLivro();
				case 6 -> listarEmprestimos();

				case 7 -> criarUsuarioMenu();
				case 8 -> alterarUsuarioMenu();
				case 9 -> excluirUsuarioMenu();

				case 10 -> {
					if (gerenteOuADM) {
						criarFuncionarioMenu(usuario);
					} else {
						System.out.println("Permissão negada: Apenas ADM ou Gerente podem criar funcionário.");
					}
				}
				case 11 -> {
					if (gerenteOuADM) {
						alterarFuncionarioMenu(usuario);
					} else {
						System.out.println("Permissão negada: Apenas ADM ou Gerente podem alterar funcionário.");
					}
				}
				case 12 -> {
					if (gerenteOuADM) {
						excluirFuncionarioMenu(usuario);
					} else {
						System.out.println("Permissão negada: Apenas ADM ou Gerente podem excluir funcionário.");
					}
				}
				case 0 -> {
					System.out.println("Encerrando...");
					return;
				}
				default -> System.out.println("Opção inválida.");
				}
			} else {
				switch (opcao) {
				case 1 -> buscarLivro();
				case 2 -> livroService.listarLivros();
				case 3 -> listarMeusEmprestimos(usuario);
				case 4 -> {
					System.out.println("Encerrando...");
					return;
				}
				default -> System.out.println("Opção inválida.");
				}
			}
		}
	}

	// Usuário
	private void criarUsuarioMenu() {
		System.out.println("Criar Usuário:");
		System.out.print("Nome: ");
		String nome = sc.nextLine();
		System.out.print("Email: ");
		String email = sc.nextLine();
		System.out.print("Telefone: ");
		String telefone = sc.nextLine();
		System.out.print("Usuário (login): ");
		String login = sc.nextLine();
		System.out.print("Senha: ");
		String senha = sc.nextLine();

		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setTelefone(telefone);

		pessoaService.criarUsuario(usuario, login, senha);
	}

	private void alterarUsuarioMenu() {
		System.out.print("ID do usuário para alterar: ");
		int id = sc.nextInt();
		sc.nextLine();

		Usuario usuario = usuarioDao.getById(id);
		if (usuario == null) {
			System.out.println("Usuário não encontrado.");
			return;
		}

		System.out.println("Novo nome (enter para manter: " + usuario.getNome() + "):");
		String nome = sc.nextLine();
		System.out.println("Novo email (enter para manter: " + usuario.getEmail() + "):");
		String email = sc.nextLine();
		System.out.println("Novo telefone (enter para manter: " + usuario.getTelefone() + "):");
		String telefone = sc.nextLine();

		if (!nome.isBlank())
			usuario.setNome(nome);
		if (!email.isBlank())
			usuario.setEmail(email);
		if (!telefone.isBlank())
			usuario.setTelefone(telefone);

		pessoaService.alterarUsuario(usuario);
	}

	private void excluirUsuarioMenu() {
		System.out.print("ID do usuário para excluir: ");
		int id = sc.nextInt();
		sc.nextLine();

		pessoaService.excluirUsuario(id);
	}

	// Funcionário
	private void criarFuncionarioMenu(PessoaLogin solicitante) {
		System.out.println("Criar Funcionário:");
		System.out.print("Nome: ");
		String nome = sc.nextLine();
		System.out.print("Email: ");
		String email = sc.nextLine();
		System.out.print("Telefone: ");
		String telefone = sc.nextLine();
		System.out.print("Matrícula: ");
		String matricula = sc.nextLine();
		System.out.print("Cargo (ADM, Gerente, Bibliotecário ou outro): ");
		String cargo = sc.nextLine();
		System.out.print("Usuário (login): ");
		String login = sc.nextLine();
		System.out.print("Senha: ");
		String senha = sc.nextLine();

		Funcionario funcionario = new Funcionario();
		funcionario.setNome(nome);
		funcionario.setEmail(email);
		funcionario.setTelefone(telefone);
		funcionario.setMatricula(matricula);
		funcionario.setCargo(cargo);

		pessoaService.criarFuncionario(solicitante, funcionario, login, senha);
	}

	private void alterarFuncionarioMenu(PessoaLogin solicitante) {
		System.out.print("ID do funcionário para alterar: ");
		int id = sc.nextInt();
		sc.nextLine();

		Funcionario funcionario = funcionarioDao.getById(id);
		if (funcionario == null) {
			System.out.println("Funcionário não encontrado.");
			return;
		}

		System.out.println("Novo nome (enter para manter: " + funcionario.getNome() + "):");
		String nome = sc.nextLine();
		System.out.println("Novo email (enter para manter: " + funcionario.getEmail() + "):");
		String email = sc.nextLine();
		System.out.println("Novo telefone (enter para manter: " + funcionario.getTelefone() + "):");
		String telefone = sc.nextLine();
		System.out.println("Nova matrícula (enter para manter: " + funcionario.getMatricula() + "):");
		String matricula = sc.nextLine();
		System.out.println("Novo cargo (enter para manter: " + funcionario.getCargo() + "):");
		String cargo = sc.nextLine();

		if (!nome.isBlank())
			funcionario.setNome(nome);
		if (!email.isBlank())
			funcionario.setEmail(email);
		if (!telefone.isBlank())
			funcionario.setTelefone(telefone);
		if (!matricula.isBlank())
			funcionario.setMatricula(matricula);
		if (!cargo.isBlank())
			funcionario.setCargo(cargo);

		pessoaService.alterarFuncionario(solicitante, funcionario);
	}

	private void excluirFuncionarioMenu(PessoaLogin solicitante) {
		System.out.print("ID do funcionário para excluir: ");
		int id = sc.nextInt();
		sc.nextLine();

		pessoaService.excluirFuncionario(solicitante, id);
	}

	// Livros
	private void cadastrarLivro() {
		System.out.print("Título: ");
		String titulo = sc.nextLine();
		System.out.print("Autor: ");
		String autor = sc.nextLine();
		System.out.print("ISBN: ");
		String isbn = sc.nextLine();
		System.out.print("Data de publicação (dd-MM-yyyy): ");
		try {
			LocalDate data = LocalDate.parse(sc.nextLine(), formatter);
			livroService.cadastrarLivro(titulo, autor, isbn, data);
		} catch (Exception e) {
			System.out.println("Data inválida. Use o formato dd-MM-yyyy.");
		}
	}

	private void alterarLivro() {
		System.out.print("ISBN do livro: ");
		String isbn = sc.nextLine();
		System.out.print("Novo título: ");
		String novoTitulo = sc.nextLine();
		System.out.print("Novo autor: ");
		String novoAutor = sc.nextLine();
		System.out.print("Nova data de publicação (dd-MM-yyyy): ");
		try {
			LocalDate novaData = LocalDate.parse(sc.nextLine(), formatter);
			livroService.alterarLivro(isbn, novoTitulo, novoAutor, novaData);
		} catch (Exception e) {
			System.out.println("Data inválida. Use o formato dd-MM-yyyy.");
		}
	}

	private void excluirLivro() {
		System.out.print("ISBN do livro a excluir: ");
		String isbn = sc.nextLine();
		livroService.excluirLivro(isbn);
	}

	private void buscarLivro() {
		System.out.print("ISBN a buscar: ");
		String isbn = sc.nextLine();
		livroService.buscarLivro(isbn);
	}

	// Empréstimos
	private void registrarEmprestimo(PessoaLogin funcionarioLogado) {
		System.out.print("ID do usuário que irá pegar o livro emprestado: ");
		int idUsuario = sc.nextInt();
		sc.nextLine();

		Usuario usuario = usuarioDao.getById(idUsuario);
		if (usuario == null) {
			System.out.println("Usuário não encontrado.");
			return;
		}

		List<String> codigos = new ArrayList<>();
		while (true) {
			System.out.print("Código do livro (ID ou ISBN, ou 'fim' para concluir): ");
			String entrada = sc.nextLine();
			if (entrada.equalsIgnoreCase("fim"))
				break;
			codigos.add(entrada);
		}

		List<Livro> livros = buscarLivrosPorCodigos(codigos);
		if (livros.isEmpty()) {
			System.out.println("Nenhum livro válido informado. Empréstimo cancelado.");
			return;
		}

		System.out.print("Data prevista para devolução (dd-MM-yyyy): ");
		try {
			LocalDate dataPrev = LocalDate.parse(sc.nextLine(), formatter);
			Emprestimo emp = new Emprestimo();
			emp.setUsuario(usuario);
			emp.setLivros(livros);
			emp.setDataEmp(LocalDate.now());
			emp.setDataPrev(dataPrev);

			emprestimoService.registrarEmprestimo(emp, funcionarioLogado);
		} catch (Exception e) {
			System.out.println("Data inválida.");
		}
	}

	private void devolverLivro() {
		System.out.print("ID do empréstimo: ");
		int idEmp = sc.nextInt();
		sc.nextLine();

		Emprestimo emp = new EmprestimoDAO().getById(idEmp);
		if (emp == null || emp.getId() == 0) {
			System.out.println("Empréstimo não encontrado.");
			return;
		}

		new EmprestimoDAO().devolver(emp);
		System.out.println("Devolução registrada com sucesso.");
	}

	private void listarEmprestimos() {
		EmprestimoDAO dao = new EmprestimoDAO();
		List<Emprestimo> emprestimos = dao.listarEmprestimos();

		if (emprestimos.isEmpty()) {
			System.out.println("Nenhum empréstimo encontrado.");
		} else {
			for (Emprestimo emp : emprestimos) {
				System.out.println(emp);
			}
		}
	}

	private List<Livro> buscarLivrosPorCodigos(List<String> codigos) {
		LivroDAO dao = new LivroDAO();
		List<Livro> livros = new ArrayList<>();

		for (String codigo : codigos) {
			Livro livro = null;
			if (codigo.matches("\\d+")) {
				livro = dao.getById(Integer.parseInt(codigo));
			} else {
				livro = dao.getByISBN(codigo);
			}
			if (livro != null && livro.getTitulo() != null) {
				livros.add(livro);
			} else {
				System.out.println("Livro não encontrado: " + codigo);
			}
		}

		return livros;
	}

	private void listarMeusEmprestimos(PessoaLogin usuario) {
		int idPessoa = usuario.getIdPessoa();

		Usuario u = usuarioDao.getById(idPessoa);
		if (u == null) {
			System.out.println("Erro ao recuperar dados do usuário.");
			return;
		}

		EmprestimoDAO dao = new EmprestimoDAO();
		List<Emprestimo> emprestimos = dao.getEmprestimosPorUsuario(u.getID());

		if (emprestimos.isEmpty()) {
			System.out.println("Você não possui empréstimos registrados.");
		} else {
			System.out.println("=== Seus Empréstimos ===");
			for (Emprestimo emp : emprestimos) {
				System.out.println(emp);
			}
		}
	}
}
