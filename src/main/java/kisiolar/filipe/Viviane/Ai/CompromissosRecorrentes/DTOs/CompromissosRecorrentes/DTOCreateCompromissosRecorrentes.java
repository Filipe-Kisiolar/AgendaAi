package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;

import java.time.LocalDate;
import java.util.List;

public class DTOCreateCompromissosRecorrentes {
        private Long id;

        private String nome;

        private String descricao;

        private String local;

        @Size(min = 1, message = "É necessário ao menos um horário.")
        @Valid
        private List<DTOCreateHorariosPorDiaBase> horariosPorDia;

        private LocalDate dataInicioRecorrencia;

        private LocalDate dataFimRecorrencia;

        private Integer intervalo;

        private ModoDeRecorrenciaEnum modoDeRecorrencia;

        private OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes;

        private boolean apenasDiasUteis;

        public DTOCreateCompromissosRecorrentes() {
        }

        public DTOCreateCompromissosRecorrentes(Long id, String nome, String descricao, String local, List<DTOCreateHorariosPorDiaBase> horariosPorDia, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia, Integer intervalo, ModoDeRecorrenciaEnum modoDeRecorrencia, OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes, boolean apenasDiasUteis) {
                this.id = id;
                this.nome = nome;
                this.descricao = descricao;
                this.local = local;
                this.horariosPorDia = horariosPorDia;
                this.dataInicioRecorrencia = dataInicioRecorrencia;
                this.dataFimRecorrencia = dataFimRecorrencia;
                this.intervalo = intervalo;
                this.modoDeRecorrencia = modoDeRecorrencia;
                this.ordenamentoDaSemanaNoMes = ordenamentoDaSemanaNoMes;
                this.apenasDiasUteis = apenasDiasUteis;
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

        public @Size(min = 1, message = "É necessário ao menos um horário.") @Valid List<DTOCreateHorariosPorDiaBase> getHorariosPorDia() {
                return horariosPorDia;
        }

        public void setHorariosPorDia(@Size(min = 1, message = "É necessário ao menos um horário.") @Valid List<DTOCreateHorariosPorDiaBase> horariosPorDia) {
                this.horariosPorDia = horariosPorDia;
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

        public OrdenamentoDaSemanaNoMesEnum getOrdenamentoDaSemanaNoMes() {
                return ordenamentoDaSemanaNoMes;
        }

        public void setOrdenamentoDaSemanaNoMes(OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
                this.ordenamentoDaSemanaNoMes = ordenamentoDaSemanaNoMes;
        }

        public boolean isApenasDiasUteis() {
                return apenasDiasUteis;
        }

        public void setApenasDiasUteis(boolean apenasDiasUteis) {
                this.apenasDiasUteis = apenasDiasUteis;
        }
}