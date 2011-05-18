package fr.dutra.tools.maven.deptree.model;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MavenDependencyTreeTgfParser extends MavenDependencyTreeLineBasedParser {

    private static enum ParsePhase {
        NODE, EDGE
    }

    private Map<String, MavenDependencyNode> nodes = new HashMap<String, MavenDependencyNode>();

    private MavenDependencyNode root;

    private ParsePhase phase = ParsePhase.NODE;

    public MavenDependencyNode parse(Reader reader) throws IOException {

        this.lines = splitLines(reader);

        if(lines.isEmpty()) {
            return null;
        }

        for(; lineIndex < this.lines.size(); lineIndex++) {
            this.parseLine();
        }

        return root;

    }

    /**
     * sample line structure:
     * <pre>
     * -1437430659 com.ibm:mqjms:jar:6.0.0:runtime
     * #
     * 1770590530 96632433 compile
     * </pre>
     */
    private void parseLine() {
        String line = this.lines.get(this.lineIndex);
        if("#".equals(line)) {
            this.phase = ParsePhase.EDGE;
        } else if(this.phase == ParsePhase.NODE) {
            String id = StringUtils.substringBefore(line, " ");
            String artifact;
            if(line.contains("active project artifact:")) {
                artifact = extractActiveProjectArtifact();
            } else {
                artifact = StringUtils.substringAfter(line, " ");
            }
            MavenDependencyNode node = parseArtifactString(artifact);
            if(root == null) {
                this.root = node;
            }
            nodes.put(id, node);
        } else {
            String parentId = StringUtils.substringBefore(line, " ");
            String childId = StringUtils.substringBetween(line, " ");
            MavenDependencyNode parent = nodes.get(parentId);
            MavenDependencyNode child = nodes.get(childId);
            parent.addChildNode(child);
        }
    }

}
