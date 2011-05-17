/**
 * 
 */
package fr.dutra.tools.maven.deptree.model;


/**
 * @author Alexandre Dutra
 *
 */
public class MavenDependencyNodeParseException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5422097493752660982L;

    /**
     * 
     */
    public MavenDependencyNodeParseException() {
    }

    /**
     * @param message
     */
    public MavenDependencyNodeParseException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public MavenDependencyNodeParseException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public MavenDependencyNodeParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
