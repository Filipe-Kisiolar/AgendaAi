package kisiolar.filipe.Viviane.Ai.Seguranca.Token;

import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class PasswordTokenUtils {

    public static String extractToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new BadRequestException("Token inválido");
        }
        return header.substring(7);
    }

    public static String hashPasswordToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Erro ao gerar hash do token", e);
        }
    }
}
