package service;

import java.sql.Connection;

import DAO.FuncionarioDAO;
import DAO.UsuarioDaoDB;
import conexao.Conexao;
import entity.Funcionario;
import entity.PessoaLogin;
import entity.Usuario;

public class PessoaService {
	private final UsuarioDaoDB usuarioDAO = new UsuarioDaoDB();
	private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

	
	public void criarUsuario(Usuario usuario, String usuarioLogin, String senha) {
		try {
			boolean sucesso = usuarioDAO.criarUsuario(usuario, usuarioLogin, senha);
			if (sucesso) {
				System.out.println("Usuário criado com sucesso.");
			} else {
				System.out.println("Falha ao criar usuário.");
			}
		} catch (Exception e) {
			System.out.println("Erro ao criar usuário: " + e.getMessage());
		}
	}

	public void alterarUsuario(Usuario usuario) {
		try {
			boolean sucesso = usuarioDAO.alterarUsuario(usuario);
			if (sucesso) {
				System.out.println("Usuário atualizado com sucesso.");
			} else {
				System.out.println("Falha ao atualizar usuário.");
			}
		} catch (Exception e) {
			System.out.println("Erro ao atualizar usuário: " + e.getMessage());
		}
	}

	public void excluirUsuario(int idPessoa) {
		try {
			boolean sucesso = usuarioDAO.excluirUsuario(idPessoa);
			if (sucesso) {
				System.out.println("Usuário excluído com sucesso.");
			} else {
				System.out.println("Falha ao excluir usuário.");
			}
		} catch (Exception e) {
			System.out.println("Erro ao excluir usuário: " + e.getMessage());
		}
	}

    public void criarFuncionario(PessoaLogin solicitante, Funcionario funcionario, String usuarioLogin, String senha) {
        // Verifique permissão no serviço antes
        String cargo = solicitante.getCargo();
        if (!"GERENTE".equalsIgnoreCase(cargo) && !"ADM".equalsIgnoreCase(cargo)) {
            System.out.println("Apenas Gerente ou ADM podem cadastrar funcionários.");
            return;
        }

        try (Connection con = Conexao.getConexao()) {
            con.setAutoCommit(false);

            funcionarioDAO.criarFuncionario(con, funcionario, usuarioLogin, senha);

            con.commit();
            System.out.println("Funcionário criado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao criar funcionário: " + e.getMessage());
            e.printStackTrace();
            // Aqui pode-se fazer rollback na conexão, mas try-with-resources fecha conexão automaticamente
        }
    }

    public void alterarFuncionario(PessoaLogin solicitante, Funcionario funcionario) {
        String cargo = solicitante.getCargo();
        if (!"GERENTE".equalsIgnoreCase(cargo) && !"ADM".equalsIgnoreCase(cargo)) {
            System.out.println("Apenas Gerente ou ADM podem alterar funcionários.");
            return;
        }

        try (Connection con = Conexao.getConexao()) {
            con.setAutoCommit(false);

            funcionarioDAO.alterarFuncionario(con, funcionario);

            con.commit();
            System.out.println("Funcionário atualizado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar funcionário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void excluirFuncionario(PessoaLogin solicitante, int idPessoa) {
        String cargo = solicitante.getCargo();
        if (!"GERENTE".equalsIgnoreCase(cargo) && !"ADM".equalsIgnoreCase(cargo)) {
            System.out.println("Apenas Gerente ou ADM podem excluir funcionários.");
            return;
        }

        try (Connection con = Conexao.getConexao()) {
            con.setAutoCommit(false);

            funcionarioDAO.excluirFuncionario(con, idPessoa);

            con.commit();
            System.out.println("Funcionário excluído com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao excluir funcionário: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
