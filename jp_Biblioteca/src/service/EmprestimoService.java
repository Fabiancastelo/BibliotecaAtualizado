package service;

import DAO.EmprestimoDAO;
import books.Emprestimo;
import books.Livro;
import entity.PessoaLogin;

public class EmprestimoService {
    private final EmprestimoDAO dao = new EmprestimoDAO();

   
    public void registrarEmprestimo(Emprestimo emprestimo, PessoaLogin funcionarioLogado) {
        String cargo = funcionarioLogado.getCargo();
        if (cargo == null) {
            System.out.println("Cargo do usuário não definido.");
            return;
        }

        if (cargo.equalsIgnoreCase("Gerente") || cargo.equalsIgnoreCase("Bibliotecário") || cargo.equalsIgnoreCase("ADM")) {
            EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    
            for (Livro livro : emprestimo.getLivros()) {
                if (emprestimoDAO.livroSemDevolucao(livro.getID())) {
                    System.out.println("O livro '" + livro.getTitulo() + "' (ISBN: " + livro.getIsbn() + ") já está emprestado e não foi devolvido.");
                    System.out.println("Empréstimo cancelado.");
                    return;
                }
            }

            int livrosAtivos = emprestimoDAO.contarEmprestimosAtivo(emprestimo.getUsuario().getID());
            int totalDepois = livrosAtivos + emprestimo.getLivros().size();

            if (totalDepois > 3) {
                System.out.println("O usuário já possui " + livrosAtivos + " livros emprestados.");
                System.out.println("Ele só pode ter até 3 livros emprestados ao mesmo tempo.");
                System.out.println("Empréstimo cancelado.");
                return;
            }

            dao.emprestar(emprestimo);
            System.out.println("Empréstimo registrado com sucesso!");
        } else {
            System.out.println("Acesso negado. Apenas funcionários autorizados podem registrar empréstimos.");
        }
    }

    public void devolver(Emprestimo emprestimo) {
        dao.devolver(emprestimo);
    }

    public Emprestimo getById(int id) {
        return dao.getById(id);
    }
}
