package kisiolar.filipe.Viviane.Ai.Usuarios.DTOs;

public class DTOUpdateUsuario {

    private String nome;

    private String email;

    private String phoneNumber;

    private String senha;

    public DTOUpdateUsuario() {
    }

    public DTOUpdateUsuario(String nome, String email, String phoneNumber, String senha) {
        this.nome = nome;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
