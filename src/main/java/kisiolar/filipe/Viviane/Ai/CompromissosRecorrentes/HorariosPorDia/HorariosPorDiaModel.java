package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia;

import jakarta.persistence.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.MonthDay;

@Entity
@Table(name = "horarios_por_dia_Compromissos_Recorrentes")
public class HorariosPorDiaModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Enumerated(EnumType.STRING)
        @Column(name = "dia_de_inicio")
        private DayOfWeek diaDaSemanaInicio;

        @Column(name = "hora_de_inicio")
        private LocalTime horaInicio;

        @Enumerated(EnumType.STRING)
        @Column(name = "dia_de_fim")
        private DayOfWeek diaDaSemanaFim;

        @Column(name = "hora_de_fim")
        private LocalTime horaFim;

        @Convert(converter = MonthDayConverter.class)
        @Column(name = "inicio_data_especifica_do_ano")
        private MonthDay inicioDataEspecificaDoAno;

        @Convert(converter = MonthDayConverter.class)
        @Column(name = "fim_data_especifica_do_ano")
        private MonthDay fimDataEspecificaDoAno;

        @Column(name = "inicio_dia_especifico_do_mes")
        private Integer inicioDiaEspecificoMes;

        @Column(name = "fim_dia_especifico_do_mes")
        private Integer fimDiaEspecificoMes;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "compromisso_recorrente_id",nullable = false) // nome da coluna na tabela compromisso
        private CompromissosRecorrentesModel compromissoRecorrente;

        public HorariosPorDiaModel() {
        }

        public HorariosPorDiaModel(Long id, DayOfWeek diaDaSemanaInicio, LocalTime horaInicio, DayOfWeek diaDaSemanaFim, LocalTime horaFim, MonthDay inicioDataEspecificaDoAno, MonthDay fimDataEspecificaDoAno, Integer inicioDiaEspecificoMes, Integer fimDiaEspecificoMes, CompromissosRecorrentesModel compromissoRecorrente) {
                this.id = id;
                this.diaDaSemanaInicio = diaDaSemanaInicio;
                this.horaInicio = horaInicio;
                this.diaDaSemanaFim = diaDaSemanaFim;
                this.horaFim = horaFim;
                this.inicioDataEspecificaDoAno = inicioDataEspecificaDoAno;
                this.fimDataEspecificaDoAno = fimDataEspecificaDoAno;
                this.inicioDiaEspecificoMes = inicioDiaEspecificoMes;
                this.fimDiaEspecificoMes = fimDiaEspecificoMes;
                this.compromissoRecorrente = compromissoRecorrente;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public DayOfWeek getDiaDaSemanaInicio() {
                return diaDaSemanaInicio;
        }

        public void setDiaDaSemanaInicio(DayOfWeek diaDaSemanaInicio) {
                this.diaDaSemanaInicio = diaDaSemanaInicio;
        }

        public LocalTime getHoraInicio() {
                return horaInicio;
        }

        public void setHoraInicio(LocalTime horaInicio) {
                this.horaInicio = horaInicio;
        }

        public DayOfWeek getDiaDaSemanaFim() {
                return diaDaSemanaFim;
        }

        public void setDiaDaSemanaFim(DayOfWeek diaDaSemanaFim) {
                this.diaDaSemanaFim = diaDaSemanaFim;
        }

        public LocalTime getHoraFim() {
                return horaFim;
        }

        public void setHoraFim(LocalTime horaFim) {
                this.horaFim = horaFim;
        }

        public MonthDay getInicioDataEspecificaDoAno() {
                return inicioDataEspecificaDoAno;
        }

        public void setInicioDataEspecificaDoAno(MonthDay inicioDataEspecificaDoAno) {
                this.inicioDataEspecificaDoAno = inicioDataEspecificaDoAno;
        }

        public MonthDay getFimDataEspecificaDoAno() {
                return fimDataEspecificaDoAno;
        }

        public void setFimDataEspecificaDoAno(MonthDay fimDataEspecificaDoAno) {
                this.fimDataEspecificaDoAno = fimDataEspecificaDoAno;
        }

        public Integer getInicioDiaEspecificoMes() {
                return inicioDiaEspecificoMes;
        }

        public void setInicioDiaEspecificoMes(Integer inicioDiaEspecificoMes) {
                this.inicioDiaEspecificoMes = inicioDiaEspecificoMes;
        }

        public Integer getFimDiaEspecificoMes() {
                return fimDiaEspecificoMes;
        }

        public void setFimDiaEspecificoMes(Integer fimDiaEspecificoMes) {
                this.fimDiaEspecificoMes = fimDiaEspecificoMes;
        }

        public CompromissosRecorrentesModel getCompromissoRecorrente() {
                return compromissoRecorrente;
        }

        public void setCompromissoRecorrente(CompromissosRecorrentesModel compromissoRecorrente) {
                this.compromissoRecorrente = compromissoRecorrente;
        }
}