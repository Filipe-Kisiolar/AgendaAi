package kisiolar.filipe.Viviane.Ai.Compromissos;

import jakarta.persistence.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class CompromissosModel {

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

    @Column(name = "dia")
    private LocalDate dia;

    @Column(name = "inicio")
    private LocalTime horaInicial;

    @Column(name = "final")
    private LocalTime horaFinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compromissorecorrente_id") // nome da coluna na tabela compromisso
    private CompromissosRecorrentesModel compromissoRecorrente;

    public CompromissosModel() {
    }

    public CompromissosModel(Long id, String nome, String descricao, String local, LocalTime horaInicial, LocalDate dia, LocalTime horaFinal, CompromissosRecorrentesModel compromissoRecorrente) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.horaInicial = horaInicial;
        this.dia = dia;
        this.horaFinal = horaFinal;
        this.compromissoRecorrente = compromissoRecorrente;
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

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public LocalTime getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }

    public CompromissosRecorrentesModel getCompromissoRecorrente() {
        return compromissoRecorrente;
    }

    public void setCompromissoRecorrente(CompromissosRecorrentesModel compromissoRecorrente) {
        this.compromissoRecorrente = compromissoRecorrente;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }
}
