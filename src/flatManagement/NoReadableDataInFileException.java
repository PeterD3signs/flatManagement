package flatManagement;

public class NoReadableDataInFileException extends Exception {
    public NoReadableDataInFileException (int LineNumber){
        super(ExceptionMessages.NoReadableDataInFIle + " Problem encountered on line " + LineNumber + ".");
    }
}