package kisiolar.filipe.Viviane.Ai.Compromissos;

public class MapperCompromissosHelper {

    public static DTOCompromissos map(CompromissosModel compromissosModel){
        DTOCompromissos dtoCompromissos = new DTOCompromissos();

        dtoCompromissos.setId(compromissosModel.getId());
        dtoCompromissos.setNome(compromissosModel.getNome());
        dtoCompromissos.setDescricao(compromissosModel.getDescricao());
        dtoCompromissos.setLocal(compromissosModel.getLocal());
        dtoCompromissos.setDia(compromissosModel.getDia());
        dtoCompromissos.setHoraInicial(compromissosModel.getHoraInicial());
        dtoCompromissos.setHoraFinal(compromissosModel.getHoraFinal());

        return dtoCompromissos;
    }

    public static CompromissosModel map(DTOCompromissos dtoCompromissos){
        CompromissosModel compromissosModel = new CompromissosModel();

        compromissosModel.setId(dtoCompromissos.getId());
        compromissosModel.setNome(dtoCompromissos.getNome());
        compromissosModel.setDescricao(dtoCompromissos.getDescricao());
        compromissosModel.setDia(dtoCompromissos.getDia());
        compromissosModel.setLocal(dtoCompromissos.getLocal());
        compromissosModel.setHoraInicial(dtoCompromissos.getHoraInicial());
        compromissosModel.setHoraFinal(dtoCompromissos.getHoraFinal());

        return compromissosModel;
    }
}
