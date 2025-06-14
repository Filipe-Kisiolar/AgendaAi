package kisiolar.filipe.Viviane.Ai.Compromissos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CompromissosRepository extends JpaRepository<CompromissosModel,Long> {

    List<CompromissosModel> findByNome(String nome);

    @Query("SELECT c FROM CompromissosModel c WHERE c.inicio >= :inicio AND c.inicio < :fim")
    List<CompromissosModel> findCompromissoByInicioBetwenn(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);


    @Modifying
    @Query("DELETE FROM CompromissosModel c WHERE c.inicio < :limite")
    void deletarCompromissosAntigos(@Param("limite") LocalDateTime limite);


}
