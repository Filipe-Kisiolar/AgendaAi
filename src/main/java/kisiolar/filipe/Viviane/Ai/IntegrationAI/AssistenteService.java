package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistenteService {

    private final ChatClient chatClient;

    public AssistenteService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String responderPergunta(String pergunta) {
        return chatClient.prompt()
                .user(pergunta)
                .call()
                .content();
    }
}
