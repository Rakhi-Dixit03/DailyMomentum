package exception;

public class HabitException extends RuntimeException {

    public HabitException(String message){
        super(message);

    }
    public HabitException(String message,Throwable cause){
        super(message,cause);

    }

}
