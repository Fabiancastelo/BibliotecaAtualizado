package entity;

import DAO.PessoaDAO;

public class Funcionario extends PessoaDAO {
	private String matricula;
	private String cargo;
	private int ID;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Funcionario() {
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public Funcionario(String nome, String email, String telefone, String matricula, String cargo) {
		super(nome, email, telefone);
		this.matricula = matricula;
		this.cargo = cargo;
	}

	@Override
	public String toString() {
		return "ID: " + ID + " | Cargo: " + cargo +" | " + super.toString() + " | Matricula: " + matricula;
	}
	
	
}
