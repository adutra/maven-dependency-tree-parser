package fr.dutra.tools.maven.deptree.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public abstract class MavenDependencyTreeLineBasedParser extends MavenDependencyTreeAbstractParser {

    protected int lineIndex = 0;

    protected List<String> lines;

    protected List<String> splitLines(final Reader reader) throws IOException {
        String line = null;
        final BufferedReader br;
        if(reader instanceof BufferedReader) {
            br = (BufferedReader) reader;
        } else {
            br = new BufferedReader(reader);
        }
        final List<String> lines = new ArrayList<String>();
        while((line = br.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

}