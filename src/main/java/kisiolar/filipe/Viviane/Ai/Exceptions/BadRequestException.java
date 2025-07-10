package kisiolar.filipe.Viviane.Ai.Exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String mensagem){
        super(mensagem);
    }
}
