package entity;

public class PessoaLogin {
	private int idPessoa;
	private String nome;
	private String usuario;
	private String cargo;

	public PessoaLogin(int idPessoa, String nome, String usuario, String cargo) {
		this.idPessoa = idPessoa;
		this.nome = nome;
		this.usuario = usuario;
		this.cargo = cargo;
	}

	public int getIdPessoa() {
		return idPessoa;
	}

	public String getNome() {
		return nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getCargo() {
		return cargo;
	}
}
