package kisiolar.filipe.Viviane.Ai.Messaging.Producer;

import kisiolar.filipe.Viviane.Ai.Messaging.DTOs.AccountCreatedMessageEmailDto;
import kisiolar.filipe.Viviane.Ai.Messaging.EmailMapper;
import kisiolar.filipe.Viviane.Ai.Messaging.Routings;
import kisiolar.filipe.Viviane.Ai.Messaging.DTOs.DTONewPasswordRequest;
import kisiolar.filipe.Viviane.Ai.Seguranca.PasswordResetTokenModel;
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

    public RabbitSender(RabbitTemplate template, TopicExchange exchange, Routings routing, EmailMapper emailMapper) {
        this.template = template;
        this.exchange = exchange;
        this.routing = routing;
        this.emailMapper = emailMapper;
    }

    public void sendAccountCreatedMessage(UsuariosModel user){
        AccountCreatedMessageEmailDto email = emailMapper.mapToAccountCreatedMessage(user);

        template.convertAndSend(exchange.getName(),routing.emailAccountCreated(),email);
    }

    public void sendNewPasswordRequest(String token,String emailTo){
        DTONewPasswordRequest passwordRequest = emailMapper.mapToNewPasswordRequest(token,emailTo);

        template.convertAndSend(exchange.getName(),routing.emailPasswordReset(),passwordRequest);
    }
}
