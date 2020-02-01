package dk.skrypalle.imbue;

/**
 * TODO JAVADOC.
 */
public class ImbueUnsatisfiedLinkError extends ImbueLinkingError {

    private static final long serialVersionUID = 5497770342644893040L;

    /**
     * TODO JAVADOC.
     */
    public ImbueUnsatisfiedLinkError(String message) {
        super(message);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueUnsatisfiedLinkError(String format, Object... args) {
        super(format, args);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueUnsatisfiedLinkError(Throwable cause, String message) {
        super(cause, message);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueUnsatisfiedLinkError(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

}
