package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.MapperCompromissos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperCompromissosRecorrentes {

    @Autowired
    MapperCompromissos mapperCompromissos;

    public CompromissosRecorrentesModel map(DTOCompromissosRecorrentes dtoCompromissosRecorrentes){
        CompromissosRecorrentesModel compromissosRecorrentesModel = new CompromissosRecorrentesModel();
        List<CompromissosModel> compromissosGerados = new ArrayList<>();
        //prevenir NullPointException
        if (dtoCompromissosRecorrentes.getCompromissosGerados() != null){
            compromissosGerados = dtoCompromissosRecorrentes.getCompromissosGerados().stream().
                    map(mapperCompromissos ::map).
            collect(Collectors.toList());
        }

        compromissosRecorrentesModel.setId(dtoCompromissosRecorrentes.getId());
        compromissosRecorrentesModel.setNome(dtoCompromissosRecorrentes.getNome());
        compromissosRecorrentesModel.setDescricao(dtoCompromissosRecorrentes.getDescricao());
        compromissosRecorrentesModel.setLocal(dtoCompromissosRecorrentes.getLocal());
        compromissosRecorrentesModel.setDiasDaSemana(dtoCompromissosRecorrentes.getDiasDaSemana());
        compromissosRecorrentesModel.setHoraInicial(dtoCompromissosRecorrentes.getHoraInicial());
        compromissosRecorrentesModel.setHoraFinal(dtoCompromissosRecorrentes.getHoraFinal());
        compromissosRecorrentesModel.setDataInicioRecorrencia(dtoCompromissosRecorrentes.getDataInicioRecorrencia());
        compromissosRecorrentesModel.setDataFimRecorrencia(dtoCompromissosRecorrentes.getDataFimRecorrencia());
        compromissosRecorrentesModel.setCompromissosGerados(compromissosGerados);

        return compromissosRecorrentesModel;
    }

    public DTOCompromissosRecorrentes map(CompromissosRecorrentesModel compromissosRecorrentesModel) {

        DTOCompromissosRecorrentes dtoCompromissosRecorrentes = new DTOCompromissosRecorrentes();
        List<DTOCompromissos> compromissosGeradosDTO = new ArrayList<>();
        //prevenir NullPointerException
        if (compromissosRecorrentesModel.getCompromissosGerados() != null) {
            compromissosGeradosDTO = compromissosRecorrentesModel.getCompromissosGerados().stream().
            map(mapperCompromissos ::map).
            collect(Collectors.toList());
        }

        dtoCompromissosRecorrentes.setId(compromissosRecorrentesModel.getId());
        dtoCompromissosRecorrentes.setNome(compromissosRecorrentesModel.getNome());
        dtoCompromissosRecorrentes.setDescricao(compromissosRecorrentesModel.getDescricao());
        dtoCompromissosRecorrentes.setLocal(compromissosRecorrentesModel.getLocal());
        dtoCompromissosRecorrentes.setDiasDaSemana(compromissosRecorrentesModel.getDiasDaSemana());
        dtoCompromissosRecorrentes.setHoraInicial(compromissosRecorrentesModel.getHoraInicial());
        dtoCompromissosRecorrentes.setHoraFinal(compromissosRecorrentesModel.getHoraFinal());
        dtoCompromissosRecorrentes.setDataInicioRecorrencia(compromissosRecorrentesModel.getDataInicioRecorrencia());
        dtoCompromissosRecorrentes.setDataFimRecorrencia(compromissosRecorrentesModel.getDataFimRecorrencia());
        dtoCompromissosRecorrentes.setCompromissosGerados(compromissosGeradosDTO);

        return dtoCompromissosRecorrentes;

    }
}
