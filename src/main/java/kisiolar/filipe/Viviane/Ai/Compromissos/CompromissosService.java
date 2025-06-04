package kisiolar.filipe.Viviane.Ai.Compromissos;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CompromissosService {
    //TODO:usar @transacional quando for fazer requisisao com mapper
    @Autowired
    CompromissosRepository compromissosRepository;

    @Autowired
    MapperCompromissos mapperCompromissos;

    @Transactional
    public List<DTOCompromissos> listarCompromissos(){
        List<CompromissosModel> lista = compromissosRepository.findAll();
        return lista.stream().
                map(mapperCompromissos::map).
                collect(Collectors.toList());

    }

    @Transactional
    public DTOCompromissos buscarCompromissoPorId(long id){
        CompromissosModel compromissosModel = compromissosRepository.getById(id);

        return mapperCompromissos.map(compromissosModel);
    }

    @Transactional
    public List<DTOCompromissos> listarCompromissosPorNome(String nome){
        List<CompromissosModel> lista = compromissosRepository.findByNome(nome);

        return lista.stream().
                map(mapperCompromissos::map).
                collect(Collectors.toList());
    }

    @Transactional
    public List<DTOCompromissos> listarCompromissosDoDia(LocalDate dia){
        List<CompromissosModel> lista = compromissosRepository.findByDia(dia);
        return lista.stream().
                map(mapperCompromissos::map).
                collect(Collectors.toList());
    }

    public DTOCompromissos criarCompromisso(DTOCompromissos dtoCompromissos){
        compromissosRepository.save(mapperCompromissos.map(dtoCompromissos));

        return dtoCompromissos;
    }

    //TODO: fazer a funcao de alterar compromisso(criar o dtoupdate e etc...)

    public void deletarCompromissoPorId(long id){
        //devo reaftorar depois para caso nao exista esse id
        compromissosRepository.deleteById(id);
    }
}
