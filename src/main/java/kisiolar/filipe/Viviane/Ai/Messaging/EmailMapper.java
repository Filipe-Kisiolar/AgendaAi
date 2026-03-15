package kisiolar.filipe.Viviane.Ai.Messaging;

import kisiolar.filipe.Viviane.Ai.Messaging.DTOs.AccountCreatedMessageEmailDto;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;

public class EmailMapper {
    public AccountCreatedMessageEmailDto mapToAccountCreatedMessage(UsuariosModel user){
        AccountCreatedMessageEmailDto email = new AccountCreatedMessageEmailDto();

        email.setUserId(user.getId());
        email.setEmailTo(user.getEmail());
        email.setUserName(user.getUsername());

        return email;
    }
}
