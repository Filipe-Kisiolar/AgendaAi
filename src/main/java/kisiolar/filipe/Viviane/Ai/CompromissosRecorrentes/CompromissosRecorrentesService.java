package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

public class CompromissosRecorrentesService{
    //TODO:usar @transacional quando for fazer requisisao com mapper
    @Autowired
    private CompromissosRecorrentesRepository compromissosRecorrentesRepository;

    @Autowired
    private MapperCompromissosRecorrentes mapperCompromissosRecorrentes;

    @Transactional
    public List<DTOCompromissosRecorrentes> listarCompromissos(){
        List<CompromissosRecorrentesModel> lista = compromissosRecorrentesRepository.findAll();

        return lista.stream().
                map(mapperCompromissosRecorrentes ::map).
                collect(Collectors.toList());
    }

    @Transactional
    public DTOCompromissosRecorrentes buscarCompromissoPorId(long id){
       CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository.findById(id).orElse(null);

       return mapperCompromissosRecorrentes.map(compromissosRecorrentesModel);
    }

    @Transactional
    public DTOCompromissosRecorrentes buscarCompromissoPorNome(String nome){
        CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository.findByNome(nome).orElse(null);

        return mapperCompromissosRecorrentes.map(compromissosRecorrentesModel);
    }

    @Transactional
    public List<DTOCompromissosRecorrentes> buscarCompromissoPorDiaDaSemana(DayOfWeek dia){
        List<CompromissosRecorrentesModel> lista = compromissosRecorrentesRepository.findByDiaDaSemana(dia);

        return  lista.stream().
                map(mapperCompromissosRecorrentes ::map).
                collect(Collectors.toList());
    }


    public DTOCompromissosRecorrentes criarCompromisso(DTOCompromissosRecorrentes dtoCompromissosRecorrentes){
        compromissosRecorrentesRepository.save(mapperCompromissosRecorrentes.map(dtoCompromissosRecorrentes));

        return dtoCompromissosRecorrentes;
    }

    //TODO: fazer a funcao de alterar compromisso(criar o dtoupdate e etc...)

    public void deletarCompromissoPorId(long id){
        //devo reaftorar depois para caso nao exista esse id
        compromissosRecorrentesRepository.deleteById(id);
    }
}
