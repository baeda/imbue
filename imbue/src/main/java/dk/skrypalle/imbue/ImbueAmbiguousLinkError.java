package dk.skrypalle.imbue;

public class ImbueAmbiguousLinkError extends ImbueLinkingError {

    private static final long serialVersionUID = -4316653701253738980L;

    public ImbueAmbiguousLinkError(String message) {
        super(message);
    }

    public ImbueAmbiguousLinkError(String format, Object... args) {
        super(format, args);
    }

    public ImbueAmbiguousLinkError(Throwable cause, String message) {
        super(cause, message);
    }

    public ImbueAmbiguousLinkError(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

}
