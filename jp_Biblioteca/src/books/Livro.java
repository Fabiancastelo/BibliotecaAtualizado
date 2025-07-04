package books;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Livro {
	private int ID;
	private String autor;
	private String titulo;
	private String isbn;
	private LocalDate anoPublicacao;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public LocalDate getAnoPublicacao() {
		return anoPublicacao;
	}

	public void setAnoPublicacao(LocalDate anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	@Override
	public String toString() {
	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	      String dataFormatada = (anoPublicacao != null) ? anoPublicacao.format(formatter) : "Data não definida";
		return "\"" + titulo + "\" - " + autor + " (" + dataFormatada + ") - ISBN: " + isbn;
	}
	
	

}
