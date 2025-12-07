package exception;

/**
 * Исключение уровня хранения данных.
 */
public class StorageException extends Exception {
    public StorageException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public StorageException(String msg) {
        super(msg);
    }
}
