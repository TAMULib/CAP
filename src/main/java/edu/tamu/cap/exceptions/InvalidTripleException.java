package edu.tamu.cap.exceptions;

public class InvalidTripleException extends Exception {

    private static final long serialVersionUID = 2195988029404370677L;

    public InvalidTripleException() {
        super();
    }

    public InvalidTripleException(String message) {
        super(message);
    }

    public InvalidTripleException(String message, Throwable e) {
        super(message, e);
    }

}
