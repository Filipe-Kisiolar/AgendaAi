package kisiolar.filipe.Viviane.Ai.Seguranca.Token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Exceptions.ExpiredTokenException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import kisiolar.filipe.Viviane.Ai.Seguranca.DTOs.JWTDadosUsuario;
import kisiolar.filipe.Viviane.Ai.Usuarios.RoleTypeEnum;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Component
@ConfigurationProperties(prefix = "jwt")
public class TokenService {

    private String segredo;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public TokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Transactional
    public String createPasswordResetToken(Long userId){
        String rawToken = UUID.randomUUID().toString();

        LocalDateTime createdAt = LocalDateTime.now();

        PasswordResetTokenModel passwordResetToken = new PasswordResetTokenModel();

        passwordResetToken.setUserId(userId);
        passwordResetToken.setTokenHash(hashPasswordToken(rawToken));
        passwordResetToken.setCreatedAt(createdAt);
        passwordResetToken.setExpiresAt(createdAt.plusMinutes(30));
        passwordResetToken.setUsed(false);

        passwordResetTokenRepository.save(passwordResetToken);

        return rawToken;
    }

    @Transactional
    public Long validatePasswordResetToken(String token){
        String hashToken = hashPasswordToken(token);

        PasswordResetTokenModel passwordResetModel = passwordResetTokenRepository.findByTokenHash(hashToken)
                .orElseThrow(() -> new ResourceNotFindException("Token não encontrado"));

        if (passwordResetModel.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ExpiredTokenException("O token expirou");
        }

        if (passwordResetModel.isUsed()){
            throw new ExpiredTokenException("Token já usado");
        }

        //It isn't necessary to save because the class is annotated with @Transactional
        passwordResetModel.setUsed(true);

        return passwordResetModel.getUserId();
    }

    public String generateToken(UsuariosModel usuario){
        Algorithm algoritimo = Algorithm.HMAC256(segredo);

        return JWT.create()
                .withSubject(usuario.getEmail())
                .withClaim("usuarioId",usuario.getId())
                .withClaim("nome",usuario.getNome())
                .withClaim("role",usuario.getRole().name())
                .withExpiresAt(Instant.now().plusSeconds(43200))
                .withIssuedAt(Instant.now())
                .withIssuer("AgendaAi")
                .sign(algoritimo);
    }

    public Optional<JWTDadosUsuario> tokenValidation(String token){

        try {
            Algorithm algoritimo = Algorithm.HMAC256(segredo);

            DecodedJWT jwt = JWT.require(algoritimo)
                    .build()
                    .verify(token);

            return Optional.of(
                    new JWTDadosUsuario(
                            jwt.getSubject(),
                            jwt.getClaim("usuarioId").asLong(),
                            jwt.getClaim("nome").asString(),
                            jwt.getClaim("role").as(RoleTypeEnum.class)
                    )
            );

        }catch (JWTVerificationException exception){

            return Optional.empty();

        }
    }

    private String hashPasswordToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Erro ao gerar hash do token", e);
        }
    }

    public String getSegredo() {
        return segredo;
    }

    public void setSegredo(String segredo) {
        this.segredo = segredo;
    }
}
