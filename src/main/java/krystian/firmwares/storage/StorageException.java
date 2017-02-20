package krystian.firmwares.storage;

/**
 * 2/19/2017 4:29 PM
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
