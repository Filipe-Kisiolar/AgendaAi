package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface CompromissosRecorrentesRepository extends JpaRepository<CompromissosRecorrentesModel,Long> {
    Optional<CompromissosRecorrentesModel> findByNome(String nome);

    List<CompromissosRecorrentesModel> findByDiasDaSemana(DayOfWeek diaDaSemana);

    @Modifying
    @Query("DELETE FROM CompromissosRecorrentesModel c WERE c.dataFimRecorrencia < CURRENT_DATE - 30")
    void deletarRecorrentesFinalizadosHaMaisDe30Dias();
}
