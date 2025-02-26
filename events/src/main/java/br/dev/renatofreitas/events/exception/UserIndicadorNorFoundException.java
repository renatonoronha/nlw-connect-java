package br.dev.renatofreitas.events.exception;

public class UserIndicadorNorFoundException extends RuntimeException{
    public UserIndicadorNorFoundException(String msg){
        super(msg);
    }
}
