package kisiolar.filipe.Viviane.Ai.Seguranca;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record PasswordDto(
        @Valid
        @NotBlank
        String password
) {
}
