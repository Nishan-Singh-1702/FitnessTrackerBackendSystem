package com.Fitness.exception;

public class APIException extends RuntimeException{
    String message;

    public APIException(){}
    public APIException(String message){
        super(message);
    }
}
