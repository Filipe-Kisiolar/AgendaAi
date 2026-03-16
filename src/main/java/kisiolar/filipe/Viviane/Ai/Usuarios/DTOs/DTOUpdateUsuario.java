package kisiolar.filipe.Viviane.Ai.Usuarios.DTOs;

public class DTOUpdateUsuario {

    private String nome;

    private String email;

    private String phoneNumber;

    public DTOUpdateUsuario() {
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
}