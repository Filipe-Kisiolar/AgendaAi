package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import java.time.LocalDateTime;

public record DTOSaidaCompromissos(

        Long id,

        String nome,

        String descricao,

        String observacoes,

        String local,

        LocalDateTime inicio,

        LocalDateTime fim
) {
    @Override
    public String toString() {
        return String.format("""
        DTOSaidaCompromissos {
            id=%s,
            nome='%s',
            descricao='%s',
            observacoes='%s',
            local='%s',
            inicio=%s,
            fim=%s
        }
        """,
        id != null ? id : "null",
        nome != null ? nome : "",
        descricao != null ? descricao : "",
        observacoes != null ? observacoes : "",
        local != null ? local : "",
        inicio != null ? inicio : "null",
        fim != null ? fim : "null"
        );
    }
}