package se.fredriksonsound.mandelbrot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class LibraryUtil {
    static boolean hasSetLibraryPath = false;
    private static class InvalidArchitectureException extends RuntimeException{
        public InvalidArchitectureException(String message) {
            super(message);
        }

    }
    private static void setLibraryPath() {
        if(hasSetLibraryPath)
            return;

        String wd;
        try {
            wd = new java.io.File( "." ).getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if ("x86".equals(System.getProperty("os.arch"))) {
            System.setProperty("java.library.path", wd + "\\lib\\x86\\");
        } else if ("amd64".equals(System.getProperty("os.arch"))) {
            System.setProperty("java.library.path", wd + "\\lib\\x64\\");
        }
        else {
            throw new InvalidArchitectureException("No suitable libraries found for this architecture (" + System.getProperty("os.arch"));
        }
    }

    public static void loadDLL(String library) {
        setLibraryPath();
        System.load(System.getProperty("java.library.path") + library);
    }

}
