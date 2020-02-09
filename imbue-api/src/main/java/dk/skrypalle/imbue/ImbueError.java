package dk.skrypalle.imbue;

/**
 * TODO JAVADOC.
 */
public class ImbueError extends Error {

    private static final long serialVersionUID = 3582662832992424725L;

    /**
     * TODO JAVADOC.
     */
    public ImbueError(String message) {
        super(message);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueError(String format, Object... args) {
        super(String.format(format, args));
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueError(Throwable cause, String message) {
        super(message, cause);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueError(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

}
