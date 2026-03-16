package kisiolar.filipe.Viviane.Ai.Seguranca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenModel,Long> {
    Optional<PasswordResetTokenModel> findByToken(String token);
}
