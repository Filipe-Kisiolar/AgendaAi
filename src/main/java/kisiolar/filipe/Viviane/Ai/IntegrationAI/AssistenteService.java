package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistenteService {

    private final ChatClient chatClient;

    private final CompromissosRecorrentesTools compromissosRecorrentesTools;

    public AssistenteService(ChatClient chatClient, CompromissosRecorrentesTools compromissosRecorrentesTools) {
        this.chatClient = chatClient;
        this.compromissosRecorrentesTools = compromissosRecorrentesTools;
    }

    public String responderPergunta(String pergunta) {
        return chatClient.prompt()
                .user(pergunta)
                .tools(compromissosRecorrentesTools)
                .call()
                .content(); // O modelo chamará automaticamente a ferramenta correta se necessário
    }
}
