package dk.skrypalle.imbue;

public class ImbueScopeError extends ImbueError {

    private static final long serialVersionUID = -7968281792858995056L;

    public ImbueScopeError(String message) {
        super(message);
    }

    public ImbueScopeError(String format, Object... args) {
        super(format, args);
    }

    public ImbueScopeError(Throwable cause, String message) {
        super(cause, message);
    }

    public ImbueScopeError(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

}
