package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface CompromissosRecorrentesRepository extends JpaRepository<CompromissosRecorrentesModel,Long> {
    Optional<CompromissosRecorrentesModel> findByNome(String nome);

    List<CompromissosRecorrentesModel> findByDiasDaSemana(DayOfWeek diaDaSemana);
}
