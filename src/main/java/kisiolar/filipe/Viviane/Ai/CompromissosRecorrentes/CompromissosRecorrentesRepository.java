package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CompromissosRecorrentesRepository extends JpaRepository<CompromissosRecorrentesModel,Long> {
    Optional<CompromissosRecorrentesModel> findByNome(String nome);

    List<CompromissosRecorrentesModel> findByDiasDaSemana(DayOfWeek diaDaSemana);

    //listar compromissos conflitantes relativos a um compromisso
    @Query("""
SELECT c FROM CompromissosRecorrentesModel c
WHERE
  c.dataInicioRecorrencia <= :dataFim AND
  c.dataFimRecorrencia >= :dataInicio AND
  EXISTS (
    SELECT d FROM c.diasDaSemana d
    WHERE d IN :dias
  ) AND (
    (:horaInicio BETWEEN c.horaInicial AND c.horaFinal)
    OR (:horaFinal BETWEEN c.horaInicial AND c.horaFinal)
    OR (c.horaInicial BETWEEN :horaInicio AND :horaFinal)
  )
""")
    List<CompromissosRecorrentesModel> buscarConflitosRecorrentes(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("dias") List<DayOfWeek> dias,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFinal") LocalTime horaFinal
    );


}
