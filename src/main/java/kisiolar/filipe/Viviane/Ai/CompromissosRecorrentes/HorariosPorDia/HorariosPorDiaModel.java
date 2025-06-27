package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia;

import jakarta.persistence.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;

@Entity
@Table(name = "horarios_por_dia_compromissos_recorrentes_model")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class HorariosPorDiaModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "compromisso_recorrente_id",nullable = false) // nome da coluna na tabela compromisso
        private CompromissosRecorrentesModel compromissoRecorrente;

        public HorariosPorDiaModel() {
        }

        public HorariosPorDiaModel(Long id, CompromissosRecorrentesModel compromissoRecorrente) {
                this.id = id;
                this.compromissoRecorrente = compromissoRecorrente;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public CompromissosRecorrentesModel getCompromissoRecorrente() {
                return compromissoRecorrente;
        }

        public void setCompromissoRecorrente(CompromissosRecorrentesModel compromissoRecorrente) {
                this.compromissoRecorrente = compromissoRecorrente;
        }
}