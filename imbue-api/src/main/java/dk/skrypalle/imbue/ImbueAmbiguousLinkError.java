package dk.skrypalle.imbue;

/**
 * TODO JAVADOC.
 */
public class ImbueAmbiguousLinkError extends ImbueLinkingError {

    private static final long serialVersionUID = -4316653701253738980L;

    /**
     * TODO JAVADOC.
     */
    public ImbueAmbiguousLinkError(String message) {
        super(message);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueAmbiguousLinkError(String format, Object... args) {
        super(format, args);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueAmbiguousLinkError(Throwable cause, String message) {
        super(cause, message);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueAmbiguousLinkError(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

}
