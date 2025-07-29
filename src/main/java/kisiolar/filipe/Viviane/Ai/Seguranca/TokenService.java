package kisiolar.filipe.Viviane.Ai.Seguranca;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import kisiolar.filipe.Viviane.Ai.Usuarios.RoleTypeEnum;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "jwt")
public class TokenService {

    private String segredo;

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

    public String getSegredo() {
        return segredo;
    }

    public void setSegredo(String segredo) {
        this.segredo = segredo;
    }
}
