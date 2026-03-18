package kisiolar.filipe.Viviane.Ai.Messaging.Producer;

import kisiolar.filipe.Viviane.Ai.Exceptions.MessageSendingException;
import kisiolar.filipe.Viviane.Ai.Messaging.DTOs.AccountCreatedMessageEmailDto;
import kisiolar.filipe.Viviane.Ai.Messaging.EmailMapper;
import kisiolar.filipe.Viviane.Ai.Messaging.Configuration.PasswordResetProperties;
import kisiolar.filipe.Viviane.Ai.Messaging.Routings;
import kisiolar.filipe.Viviane.Ai.Messaging.DTOs.DTONewPasswordRequest;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitSender {

    private final RabbitTemplate template;

    private final TopicExchange exchange;

    private final Routings routing;

    private final EmailMapper emailMapper;

    private final PasswordResetProperties passwordResetProperties;

    public RabbitSender(RabbitTemplate template, TopicExchange exchange, Routings routing, EmailMapper emailMapper, PasswordResetProperties passwordResetProperties) {
        this.template = template;
        this.exchange = exchange;
        this.routing = routing;
        this.emailMapper = emailMapper;
        this.passwordResetProperties = passwordResetProperties;
    }

    public void sendAccountCreatedMessage(UsuariosModel user) throws MessageSendingException {
        AccountCreatedMessageEmailDto email = emailMapper.mapToAccountCreatedMessage(user);

        template.convertAndSend(exchange.getName(),routing.emailAccountCreated(),email);
    }

    public void sendNewPasswordRequest(UsuariosModel user,String token)throws MessageSendingException{
        DTONewPasswordRequest passwordRequest = emailMapper.mapToNewPasswordRequest(user,token, passwordResetProperties.getPath());

        template.convertAndSend(exchange.getName(),routing.emailPasswordReset(),passwordRequest);
    }
}
