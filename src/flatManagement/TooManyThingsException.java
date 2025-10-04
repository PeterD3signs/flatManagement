package flatManagement;

public class TooManyThingsException extends Exception{
    public TooManyThingsException () {
        super( ExceptionMessages.TooManyThingsException.toString() );
    }

}
