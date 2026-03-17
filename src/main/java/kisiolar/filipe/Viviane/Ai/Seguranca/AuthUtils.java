package kisiolar.filipe.Viviane.Ai.Seguranca;

import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import kisiolar.filipe.Viviane.Ai.Seguranca.DTOs.JWTDadosUsuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static JWTDadosUsuario getUsuarioLogado() {
        Authentication autorizacao = SecurityContextHolder.getContext().getAuthentication();

        if (autorizacao != null && autorizacao.getPrincipal() instanceof JWTDadosUsuario dadosUsuario){
            return dadosUsuario;
        } else {
            throw new ResourceNotFindException("usuario nao esta logado");
        }
    }

    public static Long getIdUsuarioLogado(){
        JWTDadosUsuario dadosUsuario = getUsuarioLogado();

        return dadosUsuario.id();
    }
}