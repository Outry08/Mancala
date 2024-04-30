package mancala;

public class InvalidMoveException extends Exception{
    private static final long serialVersionUID = 4053673262215290176L;
    
    public InvalidMoveException() {
        super("Invalid Move");
    }

    public InvalidMoveException(final String message) {
        super(message);
    }

}
