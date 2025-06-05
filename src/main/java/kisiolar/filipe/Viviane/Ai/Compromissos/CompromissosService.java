package kisiolar.filipe.Viviane.Ai.Compromissos;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompromissosService {
    //TODO:usar @transacional quando for fazer requisisao com mapper
    @Autowired
    private CompromissosRepository compromissosRepository;

    @Autowired
    private CompromissosRecorrentesRepository compromissosRecorrentesRepository;

    @Autowired
    private MapperCompromissos mapperCompromissos;

    @Transactional
    public List<DTOSaidaCompromissos> listarCompromissos(){
        List<CompromissosModel> lista = compromissosRepository.findAll();
        return lista.stream().
                map(mapperCompromissos::map).
                collect(Collectors.toList());

    }

    @Transactional
    public DTOSaidaCompromissos buscarCompromissoPorId(long id){
        CompromissosModel compromissosModel = compromissosRepository.findById(id).orElse(null);

        return mapperCompromissos.map(compromissosModel);
    }

    @Transactional
    public List<DTOSaidaCompromissos> listarCompromissosPorNome(String nome){
        List<CompromissosModel> lista = compromissosRepository.findByNome(nome);

        return lista.stream().
                map(mapperCompromissos::map).
                collect(Collectors.toList());
    }

    @Transactional
    public List<DTOSaidaCompromissos> listarCompromissosDoDia(LocalDate dia){
        List<CompromissosModel> lista = compromissosRepository.findByDia(dia);
        return lista.stream().
                map(mapperCompromissos::map).
                collect(Collectors.toList());
    }

    public DTOSaidaCompromissos criarCompromisso(DTOCreateCompromissos dtoCreateCompromissos) {
        CompromissosModel compromissosModel = mapperCompromissos.map(dtoCreateCompromissos);

        if  (dtoCreateCompromissos.getCompromissoRecorrenteId() != 0) {
            CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository
                    .findById(dtoCreateCompromissos.getCompromissoRecorrenteId())
                    .orElseThrow(() -> new RuntimeException("Compromisso recorrente não encontrado"));

            compromissosModel.setCompromissoRecorrente(compromissosRecorrentesModel);
        }

        compromissosRepository.save(compromissosModel);

        return mapperCompromissos.map(compromissosModel);
    }



    public DTOSaidaCompromissos alterarCompromisso(long id,DTOUpdateCompromissos dtoUpdateCompromissos){
        CompromissosModel compromissosModel = compromissosRepository.findById(id).orElse(null);
        mapperCompromissos.atualizacao(dtoUpdateCompromissos,compromissosModel);
        compromissosRepository.save(compromissosModel);

        return mapperCompromissos.map(compromissosModel);
    }

    public void deletarCompromissoPorId(long id){
        //devo reaftorar depois para caso nao exista esse id
        compromissosRepository.deleteById(id);
    }
}
