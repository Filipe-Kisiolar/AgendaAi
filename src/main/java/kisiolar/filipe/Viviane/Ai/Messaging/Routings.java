package kisiolar.filipe.Viviane.Ai.Messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbit.routing")
public record Routings(
        String emailAccountCreated,
        String emailPasswordReset
) {
}
