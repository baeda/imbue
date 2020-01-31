package dk.skrypalle.imbue;

public class ImbueLinkingError extends ImbueError {

    private static final long serialVersionUID = 8085692524685933915L;

    public ImbueLinkingError(String message) {
        super(message);
    }

    public ImbueLinkingError(String format, Object... args) {
        super(format, args);
    }

    public ImbueLinkingError(Throwable cause, String message) {
        super(cause, message);
    }

    public ImbueLinkingError(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

}
