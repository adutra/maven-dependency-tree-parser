package fr.dutra.tools.maven.deptree.model;

import java.io.IOException;
import java.io.Reader;

public class MavenDependencyTreeTextParser extends MavenDependencyTreeLineBasedParser {

    public MavenDependencyNode parse(Reader reader) throws IOException {
        this.lines = splitLines(reader);
        if(lines.isEmpty()) {
            return null;
        }
        return parseInternal(0);
    }

    private MavenDependencyNode parseInternal(final int depth){

        //current node
        final MavenDependencyNode node = this.parseLine();

        this.lineIndex++;

        //children
        while (this.lineIndex < this.lines.size() && this.computeDepth(this.lines.get(this.lineIndex)) > depth) {
            final MavenDependencyNode child = this.parseInternal(depth + 1);
            if(node != null) {
                node.addChildNode(child);
            }
        }
        return node;

    }

    private int computeDepth(final String line) {
        int depth = 0;
        for (int i = 0; i < line.length(); i++) {
            final char c = line.charAt(i);
            switch (c){
                case ' ':
                case '|':
                case '+':
                case '\\':
                case '-':
                    continue;
                default:
                    depth = i/3;
                    break;
            }
            break;
        }
        return depth;
    }


    /**
     * sample lineIndex structure:
     * <pre>|  |  \- org.apache.activemq:activeio-core:test-jar:tests:3.1.0:compile</pre>
     * @return
     */
    private MavenDependencyNode parseLine() {
        String line = this.lines.get(this.lineIndex);
        String artifact;
        if(line.contains("active project artifact:")) {
            artifact = extractActiveProjectArtifact();
        } else {
            artifact = extractArtifact(line);
        }
        return parseArtifactString(artifact);
    }

    private String extractArtifact(String line) {
        for (int i = 0; i < line.length(); i++) {
            final char c = line.charAt(i);
            switch (c){
                case ' ':
                case '|':
                case '+':
                case '\\':
                case '-':
                    continue;
                default:
                    return line.substring(i);
            }
        }
        return null;
    }


}
