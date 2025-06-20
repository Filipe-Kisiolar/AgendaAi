package kisiolar.filipe.Viviane.Ai.UI;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesService;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.DTOCreateCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.DTORespostaCompromissoRecorrente;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.DTORespostasListasCompromissoRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.DTOUpdateCompromissosRecorrentes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;

@Controller
@RequestMapping("/ui/recorrentes")
public class CompromissosRecorrentesUIController {

    @Autowired
    private CompromissosRecorrentesService compromissosRecorrentesService;

    @GetMapping
    public String menu() {
        return "recorrentes/menu";
    }

    @GetMapping("/listarcompromissos")
    public String listarCompromissos(Model model) {
        DTORespostasListasCompromissoRecorrentes lista = compromissosRecorrentesService.listarCompromissos();
        model.addAttribute("recorrentes", lista);
        return "recorrentes/listar";
    }

    @GetMapping("/buscarcompromissoporid/{id}")
    public String buscarCompromissoPorId(@PathVariable long id, Model model) {
        DTORespostaCompromissoRecorrente dto = compromissosRecorrentesService.buscarCompromissoPorId(id);
        model.addAttribute("compromisso", dto);
        return "recorrentes/detalhe";
    }

    @GetMapping("/buscarcompromissopornome")
    public String buscarCompromissoPornome(@RequestParam String nome, Model model) {
        DTORespostaCompromissoRecorrente dto = compromissosRecorrentesService.buscarCompromissoPorNome(nome);
        model.addAttribute("compromisso", dto);
        return "recorrentes/detalhe";
    }

    @GetMapping("/criarcompromisso")
    public String formCriar(Model model) {
        model.addAttribute("compromisso", new DTOCreateCompromissosRecorrentes());
        return "recorrentes/cadastrar";
    }

    @PostMapping("/criarcompromisso")
    public String criarCompromisso(@ModelAttribute DTOCreateCompromissosRecorrentes dto) {
        compromissosRecorrentesService.criarCompromissoRecorrente(dto);
        return "recorrentes/cadastroSucesso";
    }

    @GetMapping("/alterarcompromisso/{id}")
    public String formAlterar(@PathVariable long id, Model model) {
        DTORespostaCompromissoRecorrente compromisso = compromissosRecorrentesService.buscarCompromissoPorId(id);
        model.addAttribute("compromisso", compromisso);
        model.addAttribute("update", new DTOUpdateCompromissosRecorrentes());
        return "recorrentes/alterar";
    }

    @PostMapping("/alterarcompromisso/{id}")
    public String alterarCompromisso(@PathVariable long id, DTOUpdateCompromissosRecorrentes update) {
        compromissosRecorrentesService.alterarCompromissoRecorrente(id, update);
        return "recorrentes/alterado";
    }

    @GetMapping("/deletarcompromisso/{id}")
    public String deletarcompromisso(@PathVariable long id) {
        compromissosRecorrentesService.deletarCompromissoPorId(id);
        return "recorrentes/deletado";
    }
}