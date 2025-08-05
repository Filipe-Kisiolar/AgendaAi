package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import kisiolar.filipe.Viviane.Ai.Seguranca.AuthUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/palduro")
public class ChatGPTController {

    private final AssistenteService assistenteService;

    public ChatGPTController(AssistenteService assistenteService) {
        this.assistenteService = assistenteService;
    }

    @GetMapping("/sacoduro")
    public ResponseEntity<String> teste(@RequestParam String pergunta){


        String resposta = assistenteService.responderPergunta(pergunta);

        return ResponseEntity.ok(resposta);
    }
}
