package fr.dutra.tools.maven.deptree.core;


public class MavenDependencyTreeVisitException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -8947246553563244540L;

    public MavenDependencyTreeVisitException() {
    }

    public MavenDependencyTreeVisitException(String message) {
        super(message);
    }

    public MavenDependencyTreeVisitException(Throwable cause) {
        super(cause);
    }

    public MavenDependencyTreeVisitException(String message, Throwable cause) {
        super(message, cause);
    }

}
