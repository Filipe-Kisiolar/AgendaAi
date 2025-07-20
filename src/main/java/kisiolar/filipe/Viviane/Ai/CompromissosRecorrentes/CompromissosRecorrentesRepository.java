package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompromissosRecorrentesRepository extends JpaRepository<CompromissosRecorrentesModel,Long> {

    @Query("""
    SELECT c
       FROM CompromissosRecorrentesModel c
        WHERE c.id = :compromissoId
        AND c.usuario.id = :usuarioId
    """)
    Optional<CompromissosRecorrentesModel> findByIdByUser(
            @Param("compromissoId") long compromissoId,@Param("usuarioId") long usuarioId
    );

    @Query("""
    SELECT c
      FROM CompromissosRecorrentesModel c
        WHERE c.dataFimRecorrencia >= :dataAtual
        AND c.usuario.id = :usuarioId
    """)
    List<CompromissosRecorrentesModel> listAllByUserAfterDate(
            @Param("dataAtual")LocalDate dataAtual,@Param("usuarioId") long usuarioId
    );

    @Query("""
    SELECT c
      FROM CompromissosRecorrentesModel c
        WHERE c.nome = :nome AND c.usuario.id = :usuarioId
    """)
    Optional<CompromissosRecorrentesModel> findByNomeByUser(
            @Param("nome") String nome,@Param("usuarioId") long usuarioId
    );

    boolean existsByIdAndUsuarioId(long id, long usuarioId);

    @Modifying
    @Query("DELETE FROM CompromissosRecorrentesModel c WHERE c.dataFimRecorrencia < :limite")
    void deletarCompromissosAntigos(@Param("limite") LocalDate limite);

}
