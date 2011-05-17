package fr.dutra.tools.maven.deptree.model;

import java.io.IOException;
import java.io.Reader;


public interface MavenDependencyTreeParser {

    MavenDependencyNode parse(Reader reader) throws IOException, MavenDependencyNodeParseException;

}
