package test.thread;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017-11-16.
 */
public class ExceptionToRuntimeException extends RuntimeException {

    public static RuntimeException of(Exception e) {
        return e instanceof RuntimeException ? (RuntimeException) e : new ExceptionToRuntimeException(e);
    }

    private final Exception original;

    private ExceptionToRuntimeException(Exception e) {
        super();
        this.original = e;
    }

    public Exception getOriginal(Exception e) {
        return e;
    }

    @Override
    public String getMessage() {
        return original.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return original.getLocalizedMessage();
    }

    @Override
    public Throwable getCause() {
        return original.getCause();
    }

    @Override
    public Throwable initCause(Throwable cause) {
        original.initCause(cause);
        return this;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        original.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        original.printStackTrace(s);
    }

    public StackTraceElement[] getStackTrace() {
        return original.getStackTrace();
    }

    @Override
    public String toString() {
        return original.toString();
    }
}
