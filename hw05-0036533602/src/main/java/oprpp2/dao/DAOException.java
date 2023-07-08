package oprpp2.dao;

import java.io.Serial;

public class DAOException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(String message) {
        super(message);
    }
}
