/**
 * Copyright 2011 Alexandre Dutra
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package fr.dutra.tools.maven.deptree.core;

import static org.junit.Assert.*;

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
