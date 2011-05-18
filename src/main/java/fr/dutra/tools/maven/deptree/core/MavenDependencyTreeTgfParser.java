package fr.dutra.tools.maven.deptree.core;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MavenDependencyTreeTgfParser extends MavenDependencyTreeLineBasedParser {

    private static enum ParsePhase {
        NODE, EDGE
    }

    private Map<String, MavenDependencyTreeNode> nodes = new HashMap<String, MavenDependencyTreeNode>();

    private MavenDependencyTreeNode root;

    private ParsePhase phase = ParsePhase.NODE;

    public MavenDependencyTreeNode parse(Reader reader) throws MavenDependencyTreeParseException {

        try {
            this.lines = splitLines(reader);
        } catch (IOException e) {
            throw new MavenDependencyTreeParseException(e);
        }


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
            MavenDependencyTreeNode node = parseArtifactString(artifact);
            if(root == null) {
                this.root = node;
            }
            nodes.put(id, node);
        } else {
            String parentId = StringUtils.substringBefore(line, " ");
            String childId = StringUtils.substringBetween(line, " ");
            MavenDependencyTreeNode parent = nodes.get(parentId);
            MavenDependencyTreeNode child = nodes.get(childId);
            parent.addChildNode(child);
        }
    }

}
