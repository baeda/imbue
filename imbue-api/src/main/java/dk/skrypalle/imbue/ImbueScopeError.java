package dk.skrypalle.imbue;

/**
 * TODO JAVADOC.
 */
public class ImbueScopeError extends ImbueError {

    private static final long serialVersionUID = -7968281792858995056L;

    /**
     * TODO JAVADOC.
     */
    public ImbueScopeError(String message) {
        super(message);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueScopeError(String format, Object... args) {
        super(format, args);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueScopeError(Throwable cause, String message) {
        super(cause, message);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueScopeError(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

}
