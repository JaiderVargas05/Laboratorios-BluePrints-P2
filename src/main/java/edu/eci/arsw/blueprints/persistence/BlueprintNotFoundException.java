package edu.eci.arsw.blueprints.persistence;

public class BlueprintNotFoundException extends Exception{

    public BlueprintNotFoundException(String message) {
        super(message);
    }

    public BlueprintNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
