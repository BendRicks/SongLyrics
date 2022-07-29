package dao.exception;

public class DAOException extends Exception{

    private final int exceptionCode;

    public DAOException(Throwable cause, int exceptionCode){
        super(cause);
        this.exceptionCode = exceptionCode;
    }

    public DAOException(String message, int exceptionCode){
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public int getExceptionCode(){
        return exceptionCode;
    }

}
