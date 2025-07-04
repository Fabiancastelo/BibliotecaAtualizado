package books;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import entity.Usuario;

public class Emprestimo {
	private int id;
	private List<Livro> livros;
	private Usuario usuario;
	private LocalDate dataEmp;
	private LocalDate dataPrev;
	private LocalDate dataDev;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Livro> getLivros() {
		return livros;
	}

	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDate getDataEmp() {
		return dataEmp;
	}

	public void setDataEmp(LocalDate dataEmp) {
		this.dataEmp = dataEmp;
	}

	public LocalDate getDataPrev() {
		return dataPrev;
	}

	public void setDataPrev(LocalDate dataPrev) {
		this.dataPrev = dataPrev;
	}

	public LocalDate getDataDev() {
		return dataDev;
	}

	public void setDataDev(LocalDate dataDev) {
		this.dataDev = dataDev;
	}

	@Override
	public String toString() {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	    String dataEmpStr = (dataEmp != null) ? dataEmp.format(formatter) : "N/A";
	    String dataPrevStr = (dataPrev != null) ? dataPrev.format(formatter) : "N/A";
	    String dataDevStr = (dataDev != null) ? dataDev.format(formatter) : "N/A";

	    StringBuilder livrosStr = new StringBuilder();
	    if (livros != null && !livros.isEmpty()) {
	        for (Livro livro : livros) {
	            livrosStr.append("\n  - ").append(livro.toString());
	        }
	    } else {
	        livrosStr.append("Nenhum livro");
	    }

	    return "\nEmpréstimo ID: " + id +
	           "\nUsuário: " + (usuario != null ? usuario.getNome() : "Desconhecido") +
	           "\nLivros:" + livrosStr.toString() +
	           "\nData de Empréstimo: " + dataEmpStr +
	           "\nData Prevista de Devolução: " + dataPrevStr +
	           "\nData de Devolução: " + dataDevStr;
	}

	
}
