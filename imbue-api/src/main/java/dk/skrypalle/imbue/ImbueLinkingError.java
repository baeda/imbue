package dk.skrypalle.imbue;

/**
 * TODO JAVADOC.
 */

public class ImbueLinkingError extends ImbueError {

    private static final long serialVersionUID = 8085692524685933915L;

    /**
     * TODO JAVADOC.
     */
    public ImbueLinkingError(String message) {
        super(message);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueLinkingError(String format, Object... args) {
        super(format, args);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueLinkingError(Throwable cause, String message) {
        super(cause, message);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueLinkingError(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

}
