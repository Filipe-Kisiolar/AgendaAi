package kisiolar.filipe.Viviane.Ai.ManutencaoBD;

import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LimpezaService {

    @Autowired
    private CompromissosService compromissosService;

    @Autowired
    private CompromissosRecorrentesService compromissosRecorrentesService;

    @Scheduled(cron = "0 0 3 * * *")//todos os dias as 03:00
    public void executarLimpeza(){
        compromissosService.deletarCompromissosAntigos();
        compromissosRecorrentesService.deletarCompromissosAntigos();
    }
}
