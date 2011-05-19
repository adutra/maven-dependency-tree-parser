package fr.dutra.tools.maven.deptree.core;


public interface Visitor {

    public void visit(Node tree) throws VisitException;

}
