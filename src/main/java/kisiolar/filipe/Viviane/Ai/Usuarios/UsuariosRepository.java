package kisiolar.filipe.Viviane.Ai.Usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<UsuariosModel,Long> {

    boolean existsByEmail(String email);

    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
        FROM UsuariosModel u
        WHERE u.email   = :email
          AND u.id     <> :id
    """)
    boolean usuarioDiferenteTemEmail(@Param("email") String email, @Param("id")    Long id);

    @Query("""
         SELECT u
         FROM UsuariosModel u
         WHERE u.email = :email
    """)
    Optional<UsuariosModel> findByEmail(String email);
}
