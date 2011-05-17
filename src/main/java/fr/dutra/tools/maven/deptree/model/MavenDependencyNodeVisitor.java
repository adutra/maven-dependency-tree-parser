package fr.dutra.tools.maven.deptree.model;


public interface MavenDependencyNodeVisitor {

    public void visit(MavenDependencyNode node);

}
