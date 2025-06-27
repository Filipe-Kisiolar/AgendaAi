package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.RepositoriesHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosDiaEspecificoMensal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorariosDiaEspecificoMensalRepository extends JpaRepository<HorariosDiaEspecificoMensal,Long> {
}
