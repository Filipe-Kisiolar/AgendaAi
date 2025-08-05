package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    private final CompromissosRecorrentesTools compromissosRecorrentesTools;

    private final CompromissosTools compromissosTools;

    public ChatClientConfig(CompromissosRecorrentesTools compromissosRecorrentesTools, CompromissosTools compromissosTools) {
        this.compromissosRecorrentesTools = compromissosRecorrentesTools;
        this.compromissosTools = compromissosTools;
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .defaultTools(compromissosRecorrentesTools,compromissosTools)
                .defaultSystem("voce é um assistente de compromissos " +
                        "e só deve responder perguntas relacionadas a isso," +
                        "caso haja alguma pergunta nao relacionada a isso responda " +
                        "que so pode responder perguntas relacionadas a compromissos")
                .build();
    }
}
