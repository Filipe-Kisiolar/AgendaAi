package kisiolar.filipe.Viviane.Ai.Seguranca.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DTOEmailRequest(
        @NotBlank
        @Email
        String email
) {
}