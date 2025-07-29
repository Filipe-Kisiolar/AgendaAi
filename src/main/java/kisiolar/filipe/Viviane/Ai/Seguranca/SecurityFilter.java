package kisiolar.filipe.Viviane.Ai.Seguranca;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kisiolar.filipe.Viviane.Ai.Usuarios.RoleTypeEnum;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public SecurityFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if(Strings.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){

            String token = authorizationHeader.substring("Bearer ".length());

            Optional<JWTDadosUsuario> optionalJWTDadosUsuario = tokenService.tokenValidation(token);

            if (optionalJWTDadosUsuario.isPresent()){
                JWTDadosUsuario jwtDadosUsuario = optionalJWTDadosUsuario.get();

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                jwtDadosUsuario,
                                null,
                                List.of(new SimpleGrantedAuthority(jwtDadosUsuario.role().name()))
                        );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        }else {
            filterChain.doFilter(request, response);
        }
    }
}