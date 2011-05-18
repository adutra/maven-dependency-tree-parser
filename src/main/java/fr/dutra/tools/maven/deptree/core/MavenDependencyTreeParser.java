package fr.dutra.tools.maven.deptree.core;

import java.io.Reader;


public interface MavenDependencyTreeParser {

    MavenDependencyTreeNode parse(Reader reader) throws MavenDependencyTreeParseException;

}
