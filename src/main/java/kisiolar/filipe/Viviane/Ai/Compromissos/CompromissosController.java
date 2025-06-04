package kisiolar.filipe.Viviane.Ai.Compromissos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comprmissos")
public class CompromissosController {

    @Autowired
    CompromissosService compromissosService;


    @GetMapping("/listarCompromissos")
    public ResponseEntity<?> listarCompromissos(){
        List<DTOCompromissos> listarcompromissos = compromissosService.listarCompromissos();
        if (listarcompromissos.isEmpty()){
            Map<String,Object> resposta = new HashMap<>();
            resposta.put("mensagem","ainda nao a compromissos recorrentes para criar um copromisso recorrente entre no link:");
            resposta.put("link","/compromissosrecorrentes/criarcompromisso ");
            return ResponseEntity.status(HttpStatus.OK).body(resposta);
        }else {
            return ResponseEntity.ok(listarcompromissos);
        }
    }

    @GetMapping("/buscarcompromissoporid/{id}")
    public ResponseEntity<DTOCompromissos> buscarCompromissoPorId(@PathVariable long id){
        DTOCompromissos dtoCompromissos = compromissosService.buscarCompromissoPorId(id);

        return ResponseEntity.ok(dtoCompromissos);
    }

    @GetMapping("/listarcompromissopornome/{nome}")
    public ResponseEntity<List<DTOCompromissos>> buscarCompromissoPorNome(@PathVariable String nome){
        List<DTOCompromissos> lista = compromissosService.listarCompromissosPorNome(nome);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listarcompromisosdodia/{dia}")
    public ResponseEntity<List<DTOCompromissos>> listarCompromissosDoDia(@PathVariable LocalDate dia){
        List<DTOCompromissos> lista = compromissosService.listarCompromissosDoDia(dia);

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/crarcompromisso")
    public ResponseEntity<DTOCompromissos> criarCompromisso(@RequestBody DTOCompromissos dtoCompromissos){
        compromissosService.criarCompromisso(dtoCompromissos);

        return ResponseEntity.ok(dtoCompromissos);
    }

    @PutMapping("/alterarcompromisso/{id}")
    public ResponseEntity<DTOCompromissos> alterarCompromisso(@PathVariable long id,@RequestBody DTOCompromissos dtoCompromissos){

        return ResponseEntity.ok(dtoCompromissos);
    }

    @DeleteMapping("/deletarcompromisso/{id}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long id){
        compromissosService.deletarCompromissoPorId(id);

        return ResponseEntity.noContent().build();
    }
}
