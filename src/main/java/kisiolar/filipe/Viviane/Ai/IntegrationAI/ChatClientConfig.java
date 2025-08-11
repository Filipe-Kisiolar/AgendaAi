package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    private final CompromissosRecorrentesTools compromissosRecorrentesTools;

    private final CompromissosTools compromissosTools;

    private final Instrucoes instrucoes;

    public ChatClientConfig(CompromissosRecorrentesTools compromissosRecorrentesTools, CompromissosTools compromissosTools, Instrucoes instrucoes) {
        this.compromissosRecorrentesTools = compromissosRecorrentesTools;
        this.compromissosTools = compromissosTools;
        this.instrucoes = instrucoes;
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .defaultTools(compromissosRecorrentesTools,compromissosTools,instrucoes)
                .defaultSystem("""
                    Você é um assistente de compromissos.
                    Responda apenas perguntas relacionadas a compromissos ou dias da semana,do mes ou do ano.
                    Se receber algo fora desse contexto, informe que só responde sobre compromissos.
                    Seja educado e gentil, podendo agradecer ou cumprimentar o usuário.
                    """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory()).build())
                .build();
    }

    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory
                .builder()
                .maxMessages(25)
                .build();
    }
}