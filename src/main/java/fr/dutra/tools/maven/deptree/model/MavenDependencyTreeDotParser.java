package fr.dutra.tools.maven.deptree.model;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MavenDependencyTreeDotParser extends MavenDependencyTreeLineBasedParser {

    private Map<String, MavenDependencyNode> nodes = new HashMap<String, MavenDependencyNode>();

    private MavenDependencyNode root;

    public MavenDependencyNode parse(Reader reader) throws IOException {

        this.lines = splitLines(reader);

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
        final MavenDependencyNode node = new MavenDependencyNode(
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
     * <pre>"fr.dutra.tools.maven.deptree:maven-dependency-tree-parser:jar:1.0-SNAPSHOT" -> "org.mockito:mockito-all:jar:1.8.5:test" ;</pre>
     */
    private void parseLine() {
        String line = this.lines.get(this.lineIndex);
        String parentArtifact = StringUtils.substringBetween(line, "\"");
        MavenDependencyNode parent = nodes.get(parentArtifact);
        if(parent != null) {
            String childArtifact = StringUtils.substringBetween(line, "-> \"", "\" ;");
            MavenDependencyNode child = parseArtifactString(childArtifact);
            parent.addChildNode(child);
            nodes.put(childArtifact, child);
        } else {
            throw new IllegalStateException("Cannot find parent artifact: " + parentArtifact);
        }

    }

}
