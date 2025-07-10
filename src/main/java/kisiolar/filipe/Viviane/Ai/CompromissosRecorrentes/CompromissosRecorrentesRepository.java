package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CompromissosRecorrentesRepository extends JpaRepository<CompromissosRecorrentesModel,Long> {
    Optional<CompromissosRecorrentesModel> findByNome(String nome);

    @Modifying
    @Query("DELETE FROM CompromissosRecorrentesModel c WHERE c.dataFimRecorrencia < :limite")
    void deletarCompromissosAntigos(@Param("limite") LocalDate limite);

}
