package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import kisiolar.filipe.Viviane.Ai.Seguranca.AuthUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class AssistenteService {

    private final ChatClient chatClient;

    public AssistenteService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String responderPergunta(String pergunta) {
        String usuarioId = String.valueOf(AuthUtils.getIdUsuarioLogado());

        return chatClient.prompt()
                .user(pergunta)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID,usuarioId))
                .call()
                .content();
    }
}
