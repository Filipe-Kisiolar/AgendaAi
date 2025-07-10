package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.RepositoriesHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosPadraoRelativoMensal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorariosPadraoRelativoMensalRepository extends JpaRepository<HorariosPadraoRelativoMensal,Long> {
}
