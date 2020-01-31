package dk.skrypalle.imbue;

public class ImbueUnsatisfiedLinkError extends ImbueLinkingError {

    private static final long serialVersionUID = 5497770342644893040L;

    public ImbueUnsatisfiedLinkError(String message) {
        super(message);
    }

    public ImbueUnsatisfiedLinkError(String format, Object... args) {
        super(format, args);
    }

    public ImbueUnsatisfiedLinkError(Throwable cause, String message) {
        super(cause, message);
    }

    public ImbueUnsatisfiedLinkError(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

}
