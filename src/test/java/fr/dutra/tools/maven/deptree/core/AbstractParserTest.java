package fr.dutra.tools.maven.deptree.core;

import static org.junit.Assert.*;
import fr.dutra.tools.maven.deptree.core.Node;

public abstract class AbstractParserTest {

    protected void checkNode(Node node, String groupId, String artifactId, String packaging, String version, String scope, String classifier, String description, boolean omitted) {
        assertNotNull(node);
        assertEquals(groupId, node.getGroupId());
        assertEquals(artifactId, node.getArtifactId());
        assertEquals(packaging, node.getPackaging());
        assertEquals(version, node.getVersion());
        assertEquals(scope, node.getScope());
        assertEquals(classifier, node.getClassifier());
        assertEquals(description, node.getDescription());
        assertEquals(omitted, node.isOmitted());
    }
}
