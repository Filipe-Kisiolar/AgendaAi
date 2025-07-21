package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTORespostaHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOUpdateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOCreateHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOUpdateHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOCreateHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOUpdateHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOCreateHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOUpdateHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOCreateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOUpdateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOCreateHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOUpdateHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia.HelperMapperHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.MapperCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.stereotype.Service;

import java.util.List;

import static kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum.*;

@Service
public class HorariosPorDiaService extends HorariosServiceBase {

    private final HelperMapperHorariosPorDia helperMapperHorariosPorDia;

    private final HorariosFrequenciaDiariaService horariosFrequenciaDiariaService;

    private final HorariosFrequenciaSemanalService horariosFrequenciaSemanalService;

    private final HorariosPadraoRelativoMensalService horariosPadraoRelativoMensalService;

    private final HorariosDiaEspecificoMensalService horariosDiaEspecificoMensalService;

    private final HorariosDataEspecificaAnualService horariosDataEspecificaAnualService;


    public HorariosPorDiaService(CompromissosRecorrentesRepository compromissosRecorrentesRepository, MapperCompromissosRecorrentes mapperCompromissosRecorrentes, CompromissosService compromissosService, HelperMapperHorariosPorDia helperMapperHorariosPorDia, HorariosFrequenciaDiariaService horariosFrequenciaDiariaService, HorariosFrequenciaSemanalService horariosFrequenciaSemanalService, HorariosPadraoRelativoMensalService horariosPadraoRelativoMensalService, HorariosDiaEspecificoMensalService horariosDiaEspecificoMensalService, HorariosDataEspecificaAnualService horariosDataEspecificaAnualService) {
        super(compromissosRecorrentesRepository, mapperCompromissosRecorrentes, compromissosService);
        this.helperMapperHorariosPorDia = helperMapperHorariosPorDia;
        this.horariosFrequenciaDiariaService = horariosFrequenciaDiariaService;
        this.horariosFrequenciaSemanalService = horariosFrequenciaSemanalService;
        this.horariosPadraoRelativoMensalService = horariosPadraoRelativoMensalService;
        this.horariosDiaEspecificoMensalService = horariosDiaEspecificoMensalService;
        this.horariosDataEspecificaAnualService = horariosDataEspecificaAnualService;
    }

    @Transactional
    public DTORespostaHorariosPorDia adicionarHorario(
            long compromissoRecorrenteId, long usuarioId,
            DTOCreateHorariosPorDiaBase dtoCreateHorariosPorDia
    ) {
        CompromissosRecorrentesModel compromissoRecorrente =
                compromissosRecorrentesRepository.findByIdByUser(compromissoRecorrenteId,usuarioId)
                .orElseThrow(() -> new ResourceNotFindException("Compromisso recorrente não encontrado"));

        return switch (compromissoRecorrente.getModoDeRecorrencia()) {
            case FREQUENCIA_DIARIA -> {
                if (!(dtoCreateHorariosPorDia instanceof DTOCreateHorariosFrequenciaDiaria dtoCreateHorario)) {
                    throw new BadRequestException("DTO incompatível para FREQUENCIA_DIARIA");
                }
                yield horariosFrequenciaDiariaService.adicionarHorario(compromissoRecorrente, dtoCreateHorario);
            }
            case FREQUENCIA_SEMANAL -> {
                if (!(dtoCreateHorariosPorDia instanceof DTOCreateHorariosFrequenciaSemanal dtoCreateHorario)) {
                    throw new BadRequestException("DTO incompatível para FREQUENCIA_SEMANAL");
                }
                yield horariosFrequenciaSemanalService.adicionarHorario(compromissoRecorrente, dtoCreateHorario);
            }
            case PADRAO_RELATIVO_MENSAL -> {
                if (!(dtoCreateHorariosPorDia instanceof DTOCreateHorariosPadraoRelativoMensal dtoCreateHorario)) {
                    throw new BadRequestException("DTO incompatível para PADRAO_RELATIVO_MENSAL");
                }
                yield horariosPadraoRelativoMensalService.adicionarHorario(compromissoRecorrente, dtoCreateHorario);
            }
            case DIA_ESPECIFICO_MENSAL -> {
                if (!(dtoCreateHorariosPorDia instanceof DTOCreateHorariosDiaEspecificoMensal dtoCreateHorario)) {
                    throw new BadRequestException("DTO incompatível para DIA_ESPECIFICO_MENSAL");
                }
                yield horariosDiaEspecificoMensalService.adicionarHorario(compromissoRecorrente, dtoCreateHorario);
            }
            case DATA_ESPECIFICA_ANUAL -> {
                if (!(dtoCreateHorariosPorDia instanceof DTOCreateHorariosDataEspecificaAnual dtoCreateHorario)) {
                    throw new BadRequestException("DTO incompatível para DATA_ESPECIFICA_ANUAL");
                }
                yield horariosDataEspecificaAnualService.adicionarHorario(compromissoRecorrente, dtoCreateHorario);
            }
            default -> throw new IllegalArgumentException("Modo de recorrência inválido: " + compromissoRecorrente.getModoDeRecorrencia());
        };
    }

    @Transactional
    public DTORespostaHorariosPorDia alterarHorario(
            Long compromissoRecorrenteId, Long horarioId,long usuarioId,
            DTOUpdateHorariosPorDiaBase dtoUpdateHorariosPorDia
    ) {

        CompromissosRecorrentesModel compromissoRecorrente =
                compromissosRecorrentesRepository.findByIdByUser(compromissoRecorrenteId,usuarioId)
                .orElseThrow(() -> new ResourceNotFindException("Compromisso recorrente não encontrado"));

        return switch (compromissoRecorrente.getModoDeRecorrencia()) {
            case FREQUENCIA_DIARIA -> {
                if (!(dtoUpdateHorariosPorDia instanceof DTOUpdateHorariosFrequenciaDiaria dtoUpdateHorario)) {
                    throw new BadRequestException("DTO incompatível para FREQUENCIA_DIARIA");
                }
                yield horariosFrequenciaDiariaService.alterarHorario(compromissoRecorrente, horarioId, dtoUpdateHorario);
            }
            case FREQUENCIA_SEMANAL -> {
                if (!(dtoUpdateHorariosPorDia instanceof DTOUpdateHorariosFrequenciaSemanal dtoUpdateHorario)) {
                    throw new BadRequestException("DTO incompatível para FREQUENCIA_SEMANAL");
                }
                yield horariosFrequenciaSemanalService.alterarHorario(compromissoRecorrente, horarioId, dtoUpdateHorario);
            }
            case PADRAO_RELATIVO_MENSAL -> {
                if (!(dtoUpdateHorariosPorDia instanceof DTOUpdateHorariosPadraoRelativoMensal dtoUpdateHorario)) {
                    throw new BadRequestException("DTO incompatível para PADRAO_RELATIVO_MENSAL");
                }
                yield horariosPadraoRelativoMensalService.alterarHorario(compromissoRecorrente, horarioId, dtoUpdateHorario);
            }
            case DIA_ESPECIFICO_MENSAL -> {
                if (!(dtoUpdateHorariosPorDia instanceof DTOUpdateHorariosDiaEspecificoMensal dtoUpdateHorario)) {
                    throw new BadRequestException("DTO incompatível para DIA_ESPECIFICO_MENSAL");
                }
                yield horariosDiaEspecificoMensalService.alterarHorario(compromissoRecorrente, horarioId, dtoUpdateHorario);
            }
            case DATA_ESPECIFICA_ANUAL -> {
                if (!(dtoUpdateHorariosPorDia instanceof DTOUpdateHorariosDataEspecificaAnual dtoUpdateHorario)) {
                    throw new BadRequestException("DTO incompatível para DATA_ESPECIFICA_ANUAL");
                }
                yield horariosDataEspecificaAnualService.alterarHorario(compromissoRecorrente, horarioId, dtoUpdateHorario);
            }
            default -> throw new IllegalArgumentException("Modo de recorrência inválido: " + compromissoRecorrente.getModoDeRecorrencia());
        };
    }

    @Transactional
    public List<DTORespostaCompromisso> criarCompromissosPorModoDeRecorrencia(CompromissosRecorrentesModel compromissoRecorrente){

        return switch (compromissoRecorrente.getModoDeRecorrencia()){
            case FREQUENCIA_DIARIA -> {
                yield horariosFrequenciaDiariaService.criarCompromissosPorRecorrenciaFrequenciaDiaria(compromissoRecorrente);
            }
            case FREQUENCIA_SEMANAL -> {
                yield horariosFrequenciaSemanalService.criarCompromissosPorRecorrenciaFrequenciaSemanal(compromissoRecorrente);
            }
            case PADRAO_RELATIVO_MENSAL -> {
                yield horariosPadraoRelativoMensalService.criarCompromissosPorRecorrenciaPadraoRelativoMensal(compromissoRecorrente);
            }
            case DIA_ESPECIFICO_MENSAL -> {
                yield horariosDiaEspecificoMensalService.criarCompromissosPorRecorrenciaDiaEspecificoMensal(compromissoRecorrente);
            }
            case DATA_ESPECIFICA_ANUAL -> {
                yield horariosDataEspecificaAnualService.criarCompromissosPorRecorrenciaDataEspecificaAnual(compromissoRecorrente);
            }
            default ->
                    throw new IllegalArgumentException("Modo de recorrência inválido: " + compromissoRecorrente.getModoDeRecorrencia());
        };
    }

    @Transactional
    public Long deletarHorarioPorId(
            Long compromissoRecorrenteId, Long horarioId,long usuarioId
    ) {

        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.
                findByIdByUser(compromissoRecorrenteId,usuarioId)
                .orElseThrow(() -> new ResourceNotFindException("Compromisso inexistente"));

        HorariosPorDiaModel horario = compromissoRecorrente.getHorariosPorDia()
                .stream()
                .filter(h -> h.getId().equals(horarioId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFindException("Horário não encontrado nesse compromisso"));

        long compromissosDeletados = switch (horario){
            case HorariosFrequenciaDiaria diaria when compromissoRecorrente.getModoDeRecorrencia() == FREQUENCIA_DIARIA-> {
                yield horariosFrequenciaDiariaService.apagarCompromissosAtreladosAoHorarioPorDia(diaria);
            }
            case HorariosFrequenciaSemanal semanal when compromissoRecorrente.getModoDeRecorrencia() == FREQUENCIA_SEMANAL -> {
                yield horariosFrequenciaSemanalService.apagarCompromissosAtreladosAoHorarioPorDia(semanal);
            }
            case HorariosPadraoRelativoMensal relativoMensal when compromissoRecorrente.getModoDeRecorrencia() == PADRAO_RELATIVO_MENSAL-> {
                yield horariosPadraoRelativoMensalService.apagarCompromissosAtreladosAoHorarioPorDia(relativoMensal);
            }
            case HorariosDiaEspecificoMensal mensal when compromissoRecorrente.getModoDeRecorrencia() == DIA_ESPECIFICO_MENSAL-> {
                yield horariosDiaEspecificoMensalService.apagarCompromissosAtreladosAoHorarioPorDia(mensal);

            }
            case HorariosDataEspecificaAnual anual when compromissoRecorrente.getModoDeRecorrencia() == DATA_ESPECIFICA_ANUAL -> {
                yield horariosDataEspecificaAnualService.apagarCompromissosAtreladosAoHorarioPorDia(anual);
            }
            default -> throw new IllegalArgumentException("Combinação inválida de tipo e modo de recorrência");
        };

        compromissoRecorrente.getHorariosPorDia().remove(horario);
        return compromissosDeletados;
    }

    @Transactional
    public boolean verificarConflitosComHorarioNaLista(
            ModoDeRecorrenciaEnum modoDeRecorrencia,
            HorariosPorDiaModel horarioAnalizado,List<HorariosPorDiaModel> listaHorarios){

        return switch (modoDeRecorrencia){
            case FREQUENCIA_DIARIA -> {
                if (!(horarioAnalizado instanceof HorariosFrequenciaDiaria horario)) {
                    throw new BadRequestException("DTO incompatível para FREQUENCIA_DIARIA");
                }
                List<HorariosFrequenciaDiaria> listaDiaria = listaHorarios.stream().
                        map(HorariosFrequenciaDiaria.class::cast).toList();

                yield horariosFrequenciaDiariaService.verificarConflitosComHorarioNaLista(horario, listaDiaria);
            }
            case FREQUENCIA_SEMANAL -> {
                if (!(horarioAnalizado instanceof HorariosFrequenciaSemanal horario)) {
                    throw new BadRequestException("DTO incompatível para FREQUENCIA_SEMANAL");
                }

                List<HorariosFrequenciaSemanal> listaSemanal = listaHorarios.stream()
                        .map(HorariosFrequenciaSemanal.class::cast).toList();

                yield horariosFrequenciaSemanalService.verificarConflitosComHorarioNaLista(horario, listaSemanal);
            }
            case PADRAO_RELATIVO_MENSAL -> {
                if (!(horarioAnalizado instanceof HorariosPadraoRelativoMensal horario)) {
                    throw new BadRequestException("DTO incompatível para PADRAO_RELATIVO_MENSAL");
                }

                List<HorariosPadraoRelativoMensal> listaRelativoMensal = listaHorarios.stream()
                        .map(HorariosPadraoRelativoMensal.class::cast).toList();

                yield horariosPadraoRelativoMensalService.verificarConflitosComHorarioNaLista(horario,listaRelativoMensal);
            }
            case DIA_ESPECIFICO_MENSAL -> {
                if (!(horarioAnalizado instanceof HorariosDiaEspecificoMensal horario)) {
                    throw new BadRequestException("DTO incompatível para DIA_ESPECIFICO_MENSAL");
                }

                List<HorariosDiaEspecificoMensal> listaDiaMensal = listaHorarios.stream()
                        .map(HorariosDiaEspecificoMensal.class::cast).toList();

                yield horariosDiaEspecificoMensalService.verificarConflitosComHorarioNaLista(horario,listaDiaMensal);
            }
            case DATA_ESPECIFICA_ANUAL -> {
                if (!(horarioAnalizado instanceof HorariosDataEspecificaAnual horario)) {
                    throw new BadRequestException("DTO incompatível para DATA_ESPECIFICA_ANUAL");
                }

                List<HorariosDataEspecificaAnual> listaAnual = listaHorarios.stream()
                        .map(HorariosDataEspecificaAnual.class::cast).toList();

                yield horariosDataEspecificaAnualService.verificarConflitosComHorarioNaLista(horario,listaAnual);
            }
            default -> throw new IllegalArgumentException("Modo de recorrência inválido: " + modoDeRecorrencia);
        };
    }

    public boolean verificarConflitosListaCriacaoHorarios(
            ModoDeRecorrenciaEnum modoDeRecorrencia,List<DTOCreateHorariosPorDiaBase> listaDtoHorarios
    ){
        List<HorariosPorDiaModel> listaHorariosModel = helperMapperHorariosPorDia.mapToModelList(listaDtoHorarios);

        List<HorariosPorDiaModel> listaAuxiliarVerificacao = listaHorariosModel;

        long idFalso = 1;

        for (HorariosPorDiaModel h : listaAuxiliarVerificacao){
            h.setId(idFalso);

            idFalso++;
        }

        for (HorariosPorDiaModel horario : listaHorariosModel){

            listaAuxiliarVerificacao = listaAuxiliarVerificacao.stream().filter(h -> !h.equals(horario)).toList();

            boolean temConflito =
                    verificarConflitosComHorarioNaLista(modoDeRecorrencia,horario,listaAuxiliarVerificacao);

            if (temConflito){
                return true;
            }
        }

        return false;
    }
}