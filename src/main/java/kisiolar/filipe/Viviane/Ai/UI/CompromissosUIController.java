package kisiolar.filipe.Viviane.Ai.UI;

import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ui/compromissos")
public class CompromissosUIController {

    @Autowired
    private CompromissosService compromissosService;

    @GetMapping
    public String menu() {
        return "compromissos/menu";
    }

    @GetMapping("/listarcompromissos")
    public String listarCompromissos(Model model) {
        DTORespostaListasCompromissos lista = compromissosService.listarCompromissos();
        model.addAttribute("compromissos", lista);
        return "compromissos/listar";
    }

    @GetMapping("/buscarcompromissoporid/{id}")
    public String buscarCompromissoPorId(@PathVariable long id, Model model) {
        DTORespostaCompromisso compromisso = compromissosService.buscarCompromissoPorId(id);
        model.addAttribute("compromisso", compromisso);
        return "compromissos/detalhe";
    }

    @GetMapping("/listarcompromissopornome/{nome}")
    public String buscarCompromissoPorNome(@PathVariable String nome, Model model) {
        DTORespostaListasCompromissos lista = compromissosService.listarCompromissosPorNome(nome);
        model.addAttribute("compromissos", lista);
        return "compromissos/listar";
    }

    @GetMapping("/listarcompromissosdodia/{dia}")
    public String listarCompromissosDoDia(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia, Model model) {
        DTORespostaListasCompromissos lista = compromissosService.listarCompromissosDoDia(dia);
        model.addAttribute("compromissos", lista);
        model.addAttribute("dia", dia);
        return "compromissos/dia";
    }

    @GetMapping("/listarcompromissosdasemana/{dia}")
    public String listarCompromissosDaSemana(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia, Model model) {
        Map<DayOfWeek, DTORespostaListasCompromissos> lista = compromissosService.listarCompromissosDaSemana(dia);
        model.addAttribute("dias", lista);
        model.addAttribute("diaInicial", dia);
        return "compromissos/semana";
    }

    @GetMapping("/criarcompromisso")
    public String formCriarCompromisso(Model model) {
        model.addAttribute("compromisso", new DTOCreateCompromissos());
        return "compromissos/cadastrar";
    }

    @PostMapping("/criarcompromisso")
    public String criarCompromisso(DTOCreateCompromissos dto) {
        compromissosService.criarCompromisso(dto);
        return "compromissos/cadastroSucesso";
    }

    @GetMapping("/alterarcompromisso/{id}")
    public String formAlterar(@PathVariable long id, Model model) {
        DTORespostaCompromisso compromisso = compromissosService.buscarCompromissoPorId(id);
        model.addAttribute("compromisso", compromisso);
        model.addAttribute("update", new DTOUpdateCompromissos());
        return "compromissos/alterar";
    }

    @PostMapping("/alterarcompromisso/{id}")
    public String alterarCompromisso(@PathVariable long id, DTOUpdateCompromissos update) {
        compromissosService.alterarCompromisso(id, update);
        return "compromissos/alterado";
    }

    @GetMapping("/deletarcompromisso/{id}")
    public String deletarcompromisso(@PathVariable long id) {
        compromissosService.deletarCompromissoPorId(id);
        return "compromissos/deletado";
    }
}