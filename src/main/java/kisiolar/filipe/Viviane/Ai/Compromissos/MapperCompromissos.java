package kisiolar.filipe.Viviane.Ai.Compromissos;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.MapperCompromissosRecorrentes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperCompromissos {

    @Autowired
    MapperCompromissosRecorrentes mapperCompromissosRecorrentes;

    public CompromissosModel map(DTOCompromissos dtoCompromissos){
        CompromissosModel compromissosModel = new CompromissosModel();
        CompromissosRecorrentesModel compromissosRecorrentesModel = null;
        //prevenir NullPointerException
        if (dtoCompromissos.getCompromissosRecorrente() != null){

            compromissosRecorrentesModel = mapperCompromissosRecorrentes.map(dtoCompromissos.getCompromissosRecorrente());
        }

        compromissosModel.setId(dtoCompromissos.getId());
        compromissosModel.setNome(dtoCompromissos.getNome());
        compromissosModel.setDescricao(dtoCompromissos.getDescricao());
        compromissosModel.setDia(dtoCompromissos.getDia());
        compromissosModel.setLocal(dtoCompromissos.getLocal());
        compromissosModel.setHoraInicial(dtoCompromissos.getHoraInicial());
        compromissosModel.setHoraFinal(dtoCompromissos.getHoraFinal());
        compromissosModel.setCompromissoRecorrente(compromissosRecorrentesModel);
        return compromissosModel;
    }

    public DTOCompromissos map(CompromissosModel compromissosModel){

        DTOCompromissos dtoCompromissos = new DTOCompromissos();
        DTOCompromissosRecorrentes dtoCompromissosRecorrentes = null;
        //prevenir NullPointerException
        if (compromissosModel.getCompromissoRecorrente() != null){
            dtoCompromissosRecorrentes = mapperCompromissosRecorrentes.map(compromissosModel.getCompromissoRecorrente());
        }

        dtoCompromissos.setId(compromissosModel.getId());
        dtoCompromissos.setNome(compromissosModel.getNome());
        dtoCompromissos.setDescricao(compromissosModel.getDescricao());
        dtoCompromissos.setLocal(compromissosModel.getLocal());
        dtoCompromissos.setDia(compromissosModel.getDia());
        dtoCompromissos.setHoraInicial(compromissosModel.getHoraInicial());
        dtoCompromissos.setHoraFinal(compromissosModel.getHoraFinal());
        dtoCompromissos.setCompromissosRecorrente(dtoCompromissosRecorrentes);

        return dtoCompromissos;
    }
}
