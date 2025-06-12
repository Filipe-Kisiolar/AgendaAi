package kisiolar.filipe.Viviane.Ai.Compromissos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CompromissosRepository extends JpaRepository<CompromissosModel,Long> {
    List<CompromissosModel> findByDia(LocalDate dia);

    List<CompromissosModel> findByNome(String nome);

    List<CompromissosModel> findByDiaBetween(LocalDate inicio, LocalDate fim);

}
