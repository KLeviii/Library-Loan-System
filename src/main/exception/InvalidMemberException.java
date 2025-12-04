package main.exception;

public class InvalidMemberException extends RuntimeException {
    public InvalidMemberException(String message) {
        super(message);
    }
}
