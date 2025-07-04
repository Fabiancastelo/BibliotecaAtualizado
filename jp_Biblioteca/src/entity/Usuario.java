package entity;

import DAO.PessoaDAO;

public class Usuario extends PessoaDAO {
	private int ID;

	public Usuario() {
	}

	@Override
	public String toString() {
		return "ID: " + ID + " | " + super.toString();
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

}
