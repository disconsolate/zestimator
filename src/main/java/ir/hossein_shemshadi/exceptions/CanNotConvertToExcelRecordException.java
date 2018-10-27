package ir.hossein_shemshadi.exceptions;

public class CanNotConvertToExcelRecordException extends Exception{
    public CanNotConvertToExcelRecordException() {
    }

    public CanNotConvertToExcelRecordException(String message) {
        super(message);
    }

    public CanNotConvertToExcelRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotConvertToExcelRecordException(Throwable cause) {
        super(cause);
    }

    public CanNotConvertToExcelRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
