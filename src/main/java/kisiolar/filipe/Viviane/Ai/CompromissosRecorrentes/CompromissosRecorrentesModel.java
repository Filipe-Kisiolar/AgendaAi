package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    @Column(name = "hora_de_inicio")
    private LocalTime horaInicial;

    @Column(name = "hora_de_final")
    private LocalTime horaFinal;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "dias_da_semana")
    private List<DayOfWeek> diasDaSemana;

    @Column(name = "inicio_da_recorrencia")
    private LocalDate dataInicioRecorrencia;

    @Column(name = "fim_da_recorrencia")
    private LocalDate dataFimRecorrencia;

    @OneToMany(mappedBy = "compromissoRecorrente", cascade = CascadeType.ALL)
    private List<CompromissosModel> compromissosGerados;

    public CompromissosRecorrentesModel() {
    }

    public CompromissosRecorrentesModel(Long id, String nome, String descricao, String local, LocalTime horaInicial, LocalTime horaFinal, List<DayOfWeek> diasDaSemana, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia, List<CompromissosModel> compromissosGerados) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.diasDaSemana = diasDaSemana;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.compromissosGerados = compromissosGerados;
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

    public LocalTime getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    public List<DayOfWeek> getDiasDaSemana() {
        return diasDaSemana;
    }

    public void setDiasDaSemana(List<DayOfWeek> diasDaSemana) {
        this.diasDaSemana = diasDaSemana;
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

    public List<CompromissosModel> getCompromissosGerados() {
        return compromissosGerados;
    }

    public void setCompromissosGerados(List<CompromissosModel> compromissosGerados) {
        this.compromissosGerados = compromissosGerados;
    }
}
