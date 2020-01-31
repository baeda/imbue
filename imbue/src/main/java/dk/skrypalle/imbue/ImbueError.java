package dk.skrypalle.imbue;

public abstract class ImbueError extends Error {

    private static final long serialVersionUID = 3582662832992424725L;

    protected ImbueError(String message) {
        super(message);
    }

    protected ImbueError(String format, Object... args) {
        super(String.format(format, args));
    }

    protected ImbueError(Throwable cause, String message) {
        super(message, cause);
    }

    protected ImbueError(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

}
