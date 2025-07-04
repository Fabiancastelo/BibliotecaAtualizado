package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
public static Connection getConexao() {
	String user = "root";
	String password = "root";
	String url = "jdbc:mysql://localhost:3306/jp_Biblioteca";
	
	try {
		return DriverManager.getConnection(url, user, password);
	} catch (SQLException e) {
		System.out.println("Conexão com o Banco de dados não efetuada, erro: ");
		System.out.println(e.getMessage());
		return null;
	}
}
}
