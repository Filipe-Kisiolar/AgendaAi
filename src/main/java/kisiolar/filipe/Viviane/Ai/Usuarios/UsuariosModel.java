package kisiolar.filipe.Viviane.Ai.Usuarios;

import jakarta.persistence.*;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class UsuariosModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="profile_img_url")
    private String profileImage;

    @Column(name="image_key")
    private String imageKey;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email",unique = true,nullable = false)
    private String email;

    @Column(name = "senha")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_permissao")
    private RoleTypeEnum role;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompromissosModel> compromissos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompromissosRecorrentesModel> compromissosRecorrentes;

    public UsuariosModel() {
    }

    public UsuariosModel(Long id, String profileImage, String imageKey, String nome, String email, String senha, RoleTypeEnum role, List<CompromissosModel> compromissos, List<CompromissosRecorrentesModel> compromissosRecorrentes) {
        this.id = id;
        this.profileImage = profileImage;
        this.imageKey = imageKey;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.compromissos = compromissos;
        this.compromissosRecorrentes = compromissosRecorrentes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public RoleTypeEnum getRole() {
        return role;
    }

    public void setRole(RoleTypeEnum role) {
        this.role = role;
    }

    public List<CompromissosModel> getCompromissos() {
        return compromissos;
    }

    public void setCompromissos(List<CompromissosModel> compromissos) {
        this.compromissos = compromissos;
    }

    public List<CompromissosRecorrentesModel> getCompromissosRecorrentes() {
        return compromissosRecorrentes;
    }

    public void setCompromissosRecorrentes(List<CompromissosRecorrentesModel> compromissosRecorrentes) {
        this.compromissosRecorrentes = compromissosRecorrentes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
