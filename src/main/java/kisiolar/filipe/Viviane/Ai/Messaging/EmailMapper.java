package kisiolar.filipe.Viviane.Ai.Messaging;

import kisiolar.filipe.Viviane.Ai.Messaging.DTOs.AccountCreatedMessageEmailDto;
import kisiolar.filipe.Viviane.Ai.Messaging.DTOs.DTONewPasswordRequest;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {
    public AccountCreatedMessageEmailDto mapToAccountCreatedMessage(UsuariosModel user){
        AccountCreatedMessageEmailDto email = new AccountCreatedMessageEmailDto();

        email.setUserId(user.getId());
        email.setEmailTo(user.getEmail());
        email.setUserName(user.getNome());

        return email;
    }

    public DTONewPasswordRequest mapToNewPasswordRequest(UsuariosModel user,String token,String passwordResetPath){
        DTONewPasswordRequest passwordRequest = new DTONewPasswordRequest();

        passwordRequest.setUserId(user.getId());
        passwordRequest.setUserName(user.getNome());
        passwordRequest.setEmailTo(user.getEmail());
        passwordRequest.setToken(token);
        passwordRequest.setPasswordResetPath(passwordResetPath);

        return passwordRequest;
    }
}
