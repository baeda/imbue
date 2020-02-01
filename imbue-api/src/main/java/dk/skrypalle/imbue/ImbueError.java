package dk.skrypalle.imbue;

/**
 * TODO JAVADOC.
 */
public abstract class ImbueError extends Error {

    private static final long serialVersionUID = 3582662832992424725L;

    /**
     * TODO JAVADOC.
     */
    protected ImbueError(String message) {
        super(message);
    }

    /**
     * TODO JAVADOC.
     */
    protected ImbueError(String format, Object... args) {
        super(String.format(format, args));
    }

    /**
     * TODO JAVADOC.
     */
    protected ImbueError(Throwable cause, String message) {
        super(message, cause);
    }

    /**
     * TODO JAVADOC.
     */
    protected ImbueError(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

}
