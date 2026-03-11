package kisiolar.filipe.Viviane.Ai.Messaging.Producer;

import kisiolar.filipe.Viviane.Ai.Messaging.EmailDto;
import kisiolar.filipe.Viviane.Ai.Messaging.Routings;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitSender {

    private final RabbitTemplate template;

    private final TopicExchange exchange;

    private final Routings routing;

    public RabbitSender(RabbitTemplate template, TopicExchange exchange, Routings routing) {
        this.template = template;
        this.exchange = exchange;
        this.routing = routing;
    }

    public void sendAccountConfirmation(UsuariosModel user){
        EmailDto email = new EmailDto();

        email.setUserId(user.getId());
        email.setEmailTo(user.getEmail());
        email.setUserName(user.getUsername());

        template.convertAndSend(exchange.getName(),routing.emailAccountCreated(),email);
    }

    public void sendPasswordReset(EmailDto email){
        template.convertAndSend(exchange.getName(),routing.emailPasswordReset(),email);
    }
}
