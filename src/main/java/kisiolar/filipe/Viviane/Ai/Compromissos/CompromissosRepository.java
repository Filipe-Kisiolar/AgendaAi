package kisiolar.filipe.Viviane.Ai.Compromissos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompromissosRepository extends JpaRepository<CompromissosModel,Long> {

    @Query("""
    SELECT c
       FROM CompromissosModel c
        WHERE c.id = :compromissoId
        AND c.usuario.id = :usuarioId
    """)
    Optional<CompromissosModel> findByIdByUser(
            @Param("compromissoId") long compromissoId,@Param("usuarioId") long usuarioId
    );

    @Query("""
    SELECT c
      FROM CompromissosModel c
        WHERE c.fim >= :dataAtual
        AND c.usuario.id = :usuarioId
    """)
    List<CompromissosModel> listAllByUserAfterDate(
            @Param("dataAtual") LocalDateTime dataAtual, @Param("usuarioId") long usuarioId
    );

    @Query("""
    SELECT c
      FROM CompromissosModel c
        WHERE c.nome = :nome
        AND c.usuario.id = :usuarioId
        AND c.fim >= :dataAtual
    """)
    List<CompromissosModel> findByNomeByUser(
            @Param("nome") String nome,@Param("usuarioId") long usuarioId,
            @Param("dataAtual") LocalDateTime dataAtual
    );

    @Query("""
    SELECT c
      FROM CompromissosModel c
     WHERE c.inicio >= :inicio
       AND c.inicio <  :fim
       AND c.usuario.id = :usuarioId
    """)
    List<CompromissosModel> findCompromissoByInicioBetwenn(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("usuarioId") long usuarioId);


    @Modifying
    @Query("DELETE FROM CompromissosModel c WHERE c.inicio < :limite")
    void deletarCompromissosAntigos(@Param("limite") LocalDateTime limite);


}
