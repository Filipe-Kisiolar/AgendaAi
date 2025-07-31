package kisiolar.filipe.Viviane.Ai.Seguranca;

import kisiolar.filipe.Viviane.Ai.Usuarios.RoleTypeEnum;

public record JWTDadosUsuario(String email,Long id, String nome,RoleTypeEnum role) {

}
