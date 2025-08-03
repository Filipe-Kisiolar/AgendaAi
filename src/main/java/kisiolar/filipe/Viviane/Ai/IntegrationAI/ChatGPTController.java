package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/palduro")
public class ChatGPTController {

    private ChatClient chatClient;

    public ChatGPTController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/sacoduro")
    public String teste(){
        return chatClient.prompt()
                .user("save chat fale se voce esta funcionando paizao")
                .call()
                .content();
    }
}
