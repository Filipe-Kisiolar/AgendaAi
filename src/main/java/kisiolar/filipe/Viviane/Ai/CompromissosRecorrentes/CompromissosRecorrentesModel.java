package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import jakarta.persistence.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosPorDiaModel;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "compromissos_recorrentes")
public class CompromissosRecorrentesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "local")
    private String local;

    @Column(name = "inicio_da_recorrencia")
    private LocalDate dataInicioRecorrencia;

    @Column(name = "fim_da_recorrencia")
    private LocalDate dataFimRecorrencia;

    @Column(name = "intervalo")
    private Integer intervalo;

    @Enumerated(EnumType.STRING)
    @Column(name = "modo_de_recorrencia")
    private ModoDeRecorrenciaEnum modoDeRecorrencia;

    @Column(name = "apenas_dias_uteis")
    private Boolean apenasDiasUteis;

    @OneToMany(mappedBy = "compromissoRecorrente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorariosPorDiaModel> horariosPorDia = new ArrayList<>();

    @OneToMany(mappedBy = "compromissoRecorrente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CompromissosModel> compromissosGerados;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuariosModel usuario;

    public CompromissosRecorrentesModel() {
    }

    public CompromissosRecorrentesModel(Long id, String nome, String descricao, String local, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia, Integer intervalo, ModoDeRecorrenciaEnum modoDeRecorrencia, Boolean apenasDiasUteis, List<HorariosPorDiaModel> horariosPorDia, List<CompromissosModel> compromissosGerados, UsuariosModel usuario) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.intervalo = intervalo;
        this.modoDeRecorrencia = modoDeRecorrencia;
        this.apenasDiasUteis = apenasDiasUteis;
        this.horariosPorDia = horariosPorDia;
        this.compromissosGerados = compromissosGerados;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public List<HorariosPorDiaModel> getHorariosPorDia() {
        return horariosPorDia;
    }

    public void setHorariosPorDia(List<HorariosPorDiaModel> horariosPorDiaModels) {
        this.horariosPorDia = horariosPorDiaModels;
    }

    public LocalDate getDataInicioRecorrencia() {
        return dataInicioRecorrencia;
    }

    public void setDataInicioRecorrencia(LocalDate dataInicioRecorrencia) {
        this.dataInicioRecorrencia = dataInicioRecorrencia;
    }

    public LocalDate getDataFimRecorrencia() {
        return dataFimRecorrencia;
    }

    public void setDataFimRecorrencia(LocalDate dataFimRecorrencia) {
        this.dataFimRecorrencia = dataFimRecorrencia;
    }

    public Integer getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }

    public ModoDeRecorrenciaEnum getModoDeRecorrencia() {
        return modoDeRecorrencia;
    }

    public void setModoDeRecorrencia(ModoDeRecorrenciaEnum modoDeRecorrencia) {
        this.modoDeRecorrencia = modoDeRecorrencia;
    }

    public Boolean getApenasDiasUteis() {
        return apenasDiasUteis;
    }

    public void setApenasDiasUteis(Boolean apenasDiasUteis) {
        this.apenasDiasUteis = apenasDiasUteis;
    }

    public List<CompromissosModel> getCompromissosGerados() {
        return compromissosGerados;
    }

    public void setCompromissosGerados(List<CompromissosModel> compromissosGerados) {
        this.compromissosGerados = compromissosGerados;
    }

    public UsuariosModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuariosModel usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompromissosRecorrentesModel that = (CompromissosRecorrentesModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}