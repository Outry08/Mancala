package mancala;

public class NoSuchPlayerException extends Exception{
    private static final long serialVersionUID = 7791337417655979794L;
    
    public NoSuchPlayerException() {
        super("No Such Player");
    }

    public NoSuchPlayerException(final String message) {
        super(message);
    }

}
