package kisiolar.filipe.Viviane.Ai.Compromissos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CompromissosRepository extends JpaRepository<CompromissosModel,Long> {
    List<CompromissosModel> findByDia(LocalDate dia);

    List<CompromissosModel> findByNome(String nome);

    List<CompromissosModel> findByDiaBetween(LocalDate inicio, LocalDate fim);

    //query para listar compromissos conflitantes quando for criar ou alterar compromisso
    @Query("SELECT c FROM CompromissosModel c " +
            "WHERE c.dia = :dia AND (" +
            "  (:inicio BETWEEN c.horaInicial AND c.horaFinal) OR " +
            "  (:fim BETWEEN c.horaInicial AND c.horaFinal) OR " +
            "  (c.horaInicial BETWEEN :inicio AND :fim))")
    List<CompromissosModel> buscarConflitos(
            @Param("dia") LocalDate dia,
            @Param("inicio") LocalTime inicio,
            @Param("fim") LocalTime fim
    );

}
