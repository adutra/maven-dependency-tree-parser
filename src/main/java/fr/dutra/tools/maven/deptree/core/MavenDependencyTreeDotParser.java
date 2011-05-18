package fr.dutra.tools.maven.deptree.core;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MavenDependencyTreeDotParser extends MavenDependencyTreeLineBasedParser {

    private Map<String, MavenDependencyTreeNode> nodes = new HashMap<String, MavenDependencyTreeNode>();

    private MavenDependencyTreeNode root;

    public MavenDependencyTreeNode parse(Reader reader) throws MavenDependencyTreeParseException {

        try {
            this.lines = splitLines(reader);
        } catch (IOException e) {
            throw new MavenDependencyTreeParseException(e);
        }

        if(lines.isEmpty()) {
            return null;
        }

        //root node
        this.parseFirstLine();

        if(root != null) {

            for(; lineIndex < this.lines.size() - 1; lineIndex++) {
                this.parseLine();
            }

        }

        return root;

    }

    private void parseFirstLine() {
        String str = StringUtils.substringBetween(this.lines.get(0), "\"");
        String[] tokens = StringUtils.split(str, ':');
        if(tokens.length != 4) {
            throw new IllegalStateException("Wrong number of tokens: " + tokens.length + " for first line (4 expected)");
        }
        final MavenDependencyTreeNode node = new MavenDependencyTreeNode(
            tokens[0],
            tokens[1],
            tokens[2],
            null,
            tokens[3],
            null,
            null,
            false
        );
        root = node;
        nodes.put(str, node);
        lineIndex++;
    }

    /**
     * sample line structure:
     * <pre>"fr.dutra.tools.maven.deptree.core:maven-dependency-tree-parser:jar:1.0-SNAPSHOT" -> "org.mockito:mockito-all:jar:1.8.5:test" ;</pre>
     */
    private void parseLine() {
        String line = this.lines.get(this.lineIndex);
        String parentArtifact;
        if(line.contains("->")) {
            parentArtifact = StringUtils.substringBetween(line, "\"");
        } else {
            parentArtifact = extractActiveProjectArtifact();
            line = lines.get(lineIndex);
        }
        MavenDependencyTreeNode parent = nodes.get(parentArtifact);
        if(parent != null) {
            String childArtifact;
            if(line.contains("active project artifact:")) {
                childArtifact = extractActiveProjectArtifact();
            } else {
                childArtifact = StringUtils.substringBetween(line, "-> \"", "\" ;");
            }
            MavenDependencyTreeNode child = parseArtifactString(childArtifact);
            parent.addChildNode(child);
            nodes.put(childArtifact, child);
        } else {
            throw new IllegalStateException("Cannot find parent artifact: " + parentArtifact);
        }

    }

}
