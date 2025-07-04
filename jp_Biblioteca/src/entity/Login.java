package entity;

public class Login {
    private int pessoaId;
    private String usuario;
    private String senha;

    public Login() {}

    public Login(int pessoaId, String usuario, String senha) {
        this.pessoaId = pessoaId;
        this.usuario = usuario;
        this.senha = senha;
    }

    public int getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(int pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Login" +
                "ID: " + pessoaId +
                ", Usuario: '" + usuario + '\'' +
                ", Senha: '" + senha + '\'' +
                '}';
    }
}