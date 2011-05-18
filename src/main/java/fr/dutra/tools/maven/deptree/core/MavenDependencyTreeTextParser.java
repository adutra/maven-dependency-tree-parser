package fr.dutra.tools.maven.deptree.core;

import java.io.IOException;
import java.io.Reader;

public class MavenDependencyTreeTextParser extends MavenDependencyTreeLineBasedParser {

    public MavenDependencyTreeNode parse(Reader reader) throws MavenDependencyTreeParseException {

        try {
            this.lines = splitLines(reader);
        } catch (IOException e) {
            throw new MavenDependencyTreeParseException(e);
        }

        if(lines.isEmpty()) {
            return null;
        }

        return parseInternal(0);

    }

    private MavenDependencyTreeNode parseInternal(final int depth){

        //current node
        final MavenDependencyTreeNode node = this.parseLine();

        this.lineIndex++;

        //children
        while (this.lineIndex < this.lines.size() && this.computeDepth(this.lines.get(this.lineIndex)) > depth) {
            final MavenDependencyTreeNode child = this.parseInternal(depth + 1);
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
    private MavenDependencyTreeNode parseLine() {
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
