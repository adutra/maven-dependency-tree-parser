package fr.dutra.tools.maven.deptree.core;

import java.io.Reader;


public interface Parser {

    Node parse(Reader reader) throws ParseException;

}
