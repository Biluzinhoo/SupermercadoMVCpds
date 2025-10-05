package Supermercado.Model;

public class UsuarioModel {
    private int id;
    private String nome;
    private String cpf;
    private boolean isAdmin;

    public UsuarioModel() {
    }

    public UsuarioModel(int id, String nome, String cpf, boolean isAdmin) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.isAdmin = isAdmin;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = this.nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
