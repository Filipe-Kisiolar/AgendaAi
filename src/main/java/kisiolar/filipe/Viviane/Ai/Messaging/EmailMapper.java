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
        email.setUserName(user.getUsername());

        return email;
    }

    public DTONewPasswordRequest mapToNewPasswordRequest(String token,String emailTo){
        DTONewPasswordRequest passwordRequest = new DTONewPasswordRequest();

        passwordRequest.setEmailTo(emailTo);
        passwordRequest.setToken(token);

        return passwordRequest;
    }
}
