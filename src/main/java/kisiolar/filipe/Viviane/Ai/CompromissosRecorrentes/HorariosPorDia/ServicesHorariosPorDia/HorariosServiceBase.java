package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.MapperCompromissosRecorrentes;

public abstract class HorariosServiceBase {

    protected CompromissosRecorrentesRepository compromissosRecorrentesRepository;

    protected MapperCompromissosRecorrentes mapperCompromissosRecorrentes;

    protected CompromissosService compromissosService;

    public HorariosServiceBase(CompromissosRecorrentesRepository compromissosRecorrentesRepository, MapperCompromissosRecorrentes mapperCompromissosRecorrentes, CompromissosService compromissosService) {
        this.compromissosRecorrentesRepository = compromissosRecorrentesRepository;
        this.mapperCompromissosRecorrentes = mapperCompromissosRecorrentes;
        this.compromissosService = compromissosService;
    }
}
