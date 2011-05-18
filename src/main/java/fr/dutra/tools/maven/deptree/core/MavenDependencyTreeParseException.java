/**
 * 
 */
package fr.dutra.tools.maven.deptree.core;


/**
 * @author Alexandre Dutra
 *
 */
public class MavenDependencyTreeParseException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5422097493752660982L;

    /**
     * 
     */
    public MavenDependencyTreeParseException() {
    }

    /**
     * @param message
     */
    public MavenDependencyTreeParseException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public MavenDependencyTreeParseException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public MavenDependencyTreeParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
