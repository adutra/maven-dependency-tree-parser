package fr.dutra.tools.maven.deptree.core;


public interface MavenDependencyTreeVisitor {

    public void visit(MavenDependencyTreeNode tree) throws MavenDependencyTreeVisitException;

}
