package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOSaidaHorariosPorDiaBase;
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
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.*;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelperMapperHorariosPorDia {

    private final MapperHorariosFrequenciaSemanal mapperSemanal;
    private final MapperHorariosFrequenciaDiaria mapperDiaria;
    private final MapperHorariosPadraoRelativoMensal mapperRelativoMensal;
    private final MapperHorariosDiaEspecificoMensal mapperDiaEspecificoMensal;
    private final MapperHorariosDataEspecificaAnual mapperAnual;

    public HelperMapperHorariosPorDia(
            MapperHorariosFrequenciaSemanal mapperSemanal,
            MapperHorariosFrequenciaDiaria mapperDiaria,
            MapperHorariosPadraoRelativoMensal mapperRelativoMensal,
            MapperHorariosDiaEspecificoMensal mapperDiaEspecificoMensal,
            MapperHorariosDataEspecificaAnual mapperAnual
    ) {
        this.mapperSemanal = mapperSemanal;
        this.mapperDiaria = mapperDiaria;
        this.mapperRelativoMensal = mapperRelativoMensal;
        this.mapperDiaEspecificoMensal = mapperDiaEspecificoMensal;
        this.mapperAnual = mapperAnual;
    }

    public HorariosPorDiaModel mapToModel(DTOCreateHorariosPorDiaBase dtoHorariosBase) {
        return switch (dtoHorariosBase) {
            case DTOCreateHorariosPadraoRelativoMensal relativoMensal ->
                    mapperRelativoMensal.mapToModel(relativoMensal);

            case DTOCreateHorariosFrequenciaSemanal semanal ->
                    mapperSemanal.mapToModel(semanal);

            case DTOCreateHorariosFrequenciaDiaria diaria ->
                    mapperDiaria.mapToModel(diaria);

            case DTOCreateHorariosDiaEspecificoMensal especificoMensalmensal ->
                    mapperDiaEspecificoMensal.mapToModel(especificoMensalmensal);

            case DTOCreateHorariosDataEspecificaAnual anual ->
                    mapperAnual.mapToModel(anual);

            default -> throw new ResourceNotFindException("Tipo desconhecido de DTO de Horário");
        };
    }

    public void atualizacao(DTOUpdateHorariosPorDiaBase dto, HorariosPorDiaModel entity) {
        switch (dto) {
            case DTOUpdateHorariosPadraoRelativoMensal relativoMensal when entity instanceof HorariosPadraoRelativoMensal model ->
                mapperRelativoMensal.atualizacao(relativoMensal, model);

            case DTOUpdateHorariosFrequenciaSemanal semanal when entity instanceof HorariosFrequenciaSemanal model ->
                    mapperSemanal.atualizacao(semanal, model);

            case DTOUpdateHorariosFrequenciaDiaria diaria when entity instanceof HorariosFrequenciaDiaria model ->
                    mapperDiaria.atualizacao(diaria, model);

            case DTOUpdateHorariosDiaEspecificoMensal especificoMensal when entity instanceof HorariosDiaEspecificoMensal model ->
                    mapperDiaEspecificoMensal.atualizacao(especificoMensal, model);

            case DTOUpdateHorariosDataEspecificaAnual anual when entity instanceof HorariosDataEspecificaAnual model ->
                    mapperAnual.atualizacao(anual, model);

            default -> throw new ResourceNotFindException("Tipo desconhecido de DTO ou Model para atualização");
        }
    }


    public DTOSaidaHorariosPorDiaBase mapToModel(HorariosPorDiaModel horariosBase) {
        return switch (horariosBase) {
            case HorariosPadraoRelativoMensal relativoMensal ->
                    mapperRelativoMensal.mapToDto(relativoMensal);

            case HorariosFrequenciaSemanal semanal ->
                    mapperSemanal.mapToDto(semanal);

            case HorariosFrequenciaDiaria diaria ->
                    mapperDiaria.mapToDto(diaria);

            case HorariosDiaEspecificoMensal especificoMensalmensal ->
                    mapperDiaEspecificoMensal.mapToDto(especificoMensalmensal);

            case HorariosDataEspecificaAnual anual ->
                    mapperAnual.mapToDto(anual);

            default -> throw new ResourceNotFindException("Tipo desconhecido de DTO de Horário");
        };
    }


    public List<HorariosPorDiaModel> mapToModelList(List<DTOCreateHorariosPorDiaBase> listaDTOsBase){
        return listaDTOsBase.stream().map(this::mapToModel).toList();
    }

    public List<DTOSaidaHorariosPorDiaBase> mapToDtoList(List<HorariosPorDiaModel> listaModelBase){
        return listaModelBase.stream().map(this::mapToModel).toList();
    }
}