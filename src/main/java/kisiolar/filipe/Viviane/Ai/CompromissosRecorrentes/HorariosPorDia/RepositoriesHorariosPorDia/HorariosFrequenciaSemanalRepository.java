package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.RepositoriesHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosFrequenciaSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorariosFrequenciaSemanalRepository extends JpaRepository<HorariosFrequenciaSemanal,Long> {
}
