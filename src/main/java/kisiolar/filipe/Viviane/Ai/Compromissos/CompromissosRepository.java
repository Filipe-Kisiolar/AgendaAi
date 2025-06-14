package kisiolar.filipe.Viviane.Ai.Compromissos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CompromissosRepository extends JpaRepository<CompromissosModel,Long> {
    List<CompromissosModel> findByDia(LocalDate dia);

    List<CompromissosModel> findByNome(String nome);

    List<CompromissosModel> findByDiaBetween(LocalDate inicio, LocalDate fim);

    @Modifying
    @Query("DELETE FROM CompromissosModel c WHERE c.dataHora < CURRENT_TIMESTAMP - 30")
    void deletarCompromissosAntigos();

}
