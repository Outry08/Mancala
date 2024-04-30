package mancala;

public class PitNotFoundException extends Exception{
    private static final long serialVersionUID = 7971644609546703985L;
    
    public PitNotFoundException() {
        super("PitNotFound");
    }

    public PitNotFoundException(final String message) {
        super(message);
    }

}
