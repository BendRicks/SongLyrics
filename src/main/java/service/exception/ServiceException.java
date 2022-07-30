package service.exception;

public class ServiceException extends Exception{

    private int exceptionCode;

    public ServiceException(Throwable cause){
        super(cause);
    }

    public ServiceException(String message, int exceptionCode){
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public boolean isDAO(){
        return getCause() != null;
    }

    public int getExceptionCode(){
        return exceptionCode;
    }

}
