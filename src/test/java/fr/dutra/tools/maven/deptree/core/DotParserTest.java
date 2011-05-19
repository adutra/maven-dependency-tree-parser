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
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.dutra.tools.maven.deptree.core.Node;
import fr.dutra.tools.maven.deptree.core.DotParser;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DotParser.class)
public class DotParserTest extends AbstractParserTest {

    @Mock
    private BufferedReader br;

    @Mock
    private Reader r;

    private DotParser parser = new DotParser();

    @Before
    public void setUp() throws Exception {
        PowerMockito.whenNew(BufferedReader.class).withArguments(r).thenReturn(br);
    }

    /*
     * digraph "com.acme.org:foo-integration:jar:2.0" {
    "com.acme.org:foo-integration:jar:2.0" -> "com.acme.org:commons-integration:jar:2.39.0:compile" ;
     */
    @Test
    public void test_root_node_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("}").
        thenReturn(null);
        Node parent = parser.parse(r);
        checkNode(parent, "com.acme.org", "foo-parent", "jar", "1.0", null, null, null, false);
        assertEquals(0, parent.getChildNodes().size());
    }

    @Test
    public void test_5_tokens_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("\t\"com.acme.org:foo-parent:jar:1.0\" -> \"com.acme.org:foo-child:jar:2.0:compile\" ; ").
        thenReturn("}").
        thenReturn(null);
        Node parent = parser.parse(r);
        Node child = checkParentAndGetFirstChild(parent);
        checkNode(child, "com.acme.org", "foo-child", "jar", "2.0", "compile", null, null, false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_6_tokens_with_description_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("\t\"com.acme.org:foo-parent:jar:1.0\" -> \"com.acme.org:foo:jar:4.4:test (scope not updated to compile)\" ; ").
        thenReturn("}").
        thenReturn(null);
        Node parent = parser.parse(r);
        Node child = checkParentAndGetFirstChild(parent);
        checkNode(child, "com.acme.org", "foo", "jar", "4.4", "test", null, "scope not updated to compile", false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_6_tokens_with_classifier_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("\t\"com.acme.org:foo-parent:jar:1.0\" -> \"com.acme.org:foo:jar:win-32:4.4:test\" ; ").
        thenReturn("}").
        thenReturn(null);
        Node parent = parser.parse(r);
        Node child = checkParentAndGetFirstChild(parent);
        checkNode(child, "com.acme.org", "foo", "jar", "4.4", "test", "win-32", null, false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_7_tokens_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("\t\"com.acme.org:foo-parent:jar:1.0\" -> \"com.acme.org:foo:jar:win-32:4.4:test (version managed from 2.1.3)\" ; ").
        thenReturn("}").
        thenReturn(null);
        Node parent = parser.parse(r);
        Node child = checkParentAndGetFirstChild(parent);
        checkNode(child, "com.acme.org", "foo", "jar", "4.4", "test", "win-32", "version managed from 2.1.3", false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_omitted_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("\t\"com.acme.org:foo-parent:jar:1.0\" -> \"(com.acme.org:foo:jar:4.8.2:test - omitted for conflict with 4.8.1)\" ; ").
        thenReturn("}").
        thenReturn(null);
        Node parent = parser.parse(r);
        Node child = checkParentAndGetFirstChild(parent);
        checkNode(child, "com.acme.org", "foo", "jar", "4.8.2", "test", null, "omitted for conflict with 4.8.1", true);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test(expected=IllegalStateException.class)
    public void test_8_tokens_should_fail() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo:jar:win-32:4.4:test:wtf (version managed from 2.1.3)\" {").
        thenReturn("}").
        thenReturn(null);
        parser.parse(r);
    }

    @Test(expected=IllegalStateException.class)
    public void test_3_tokens_should_fail() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo:jar\" {").
        thenReturn("}").
        thenReturn(null);
        parser.parse(r);
    }

    @Test(expected=IllegalStateException.class)
    public void test_unknown_parent_should_fail() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("\t\"com.acme.org:bar-parent:jar:1.0\" -> \"com.acme.org:bar-child:jar:2.0\" ; ").
        thenReturn("}").
        thenReturn(null);
        parser.parse(r);
    }

    @Test
    public void test_active_project_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").

        //parent -> active child
        thenReturn("\t\"com.acme.org:foo-parent:jar:1.0\" -> \"active project artifact:").
        thenReturn("\tartifact = active project artifact:").
        thenReturn("\tartifact = com.acme.org:foo-child:jar:2.0:compile;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-child:jar:2.0 @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-child/pom.xml;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-child:jar:2.0 @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-child/pom.xml\" ;").

        //active child -> normal grand child
        thenReturn("\t\"active project artifact:").
        thenReturn("\tartifact = active project artifact:").
        thenReturn("\tartifact = com.acme.org:foo-child:jar:2.0:compile;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-child:jar:2.0 @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-child/pom.xml;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-child:jar:2.0 @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-child/pom.xml\" -> \"com.acme.org:foo-grand-child-normal:jar:3.0:compile\" ;").

        //active child -> active grand child
        thenReturn("\t\"active project artifact:").
        thenReturn("\tartifact = active project artifact:").
        thenReturn("\tartifact = com.acme.org:foo-child:jar:2.0:compile;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-child:jar:2.0 @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-child/pom.xml;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-child:jar:2.0 @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-child/pom.xml\" -> \"active project artifact:").
        thenReturn("\tartifact = active project artifact:").
        thenReturn("\tartifact = com.acme.org:foo-grand-child-active:jar:3.0:compile;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-grand-child-active:jar:3.0 @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-grand-child-active/pom.xml;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-grand-child-active:jar:3.0 @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-grand-child-active/pom.xml\" ;").
        thenReturn("}").

        thenReturn(null);

        Node parent = parser.parse(r);
        checkNode(parent, "com.acme.org", "foo-parent", "jar", "1.0", null, null, null, false);
        assertEquals(1, parent.getChildNodes().size());
        Node child = parent.getFirstChildNode();
        checkNode(child, "com.acme.org", "foo-child", "jar", "2.0", "compile", null, null, false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());

        assertEquals(2, child.getChildNodes().size());

        Node grandChildNormal = child.getFirstChildNode();
        checkNode(grandChildNormal, "com.acme.org", "foo-grand-child-normal", "jar", "3.0", "compile", null, null, false);
        assertNotNull(grandChildNormal.getParent());
        assertSame(child, grandChildNormal.getParent());

        Node grandChildActive = child.getLastChildNode();
        checkNode(grandChildActive, "com.acme.org", "foo-grand-child-active", "jar", "3.0", "compile", null, null, false);
        assertNotNull(grandChildActive.getParent());
        assertSame(child, grandChildActive.getParent());

    }

    private Node checkParentAndGetFirstChild(Node parent) {
        checkNode(parent, "com.acme.org", "foo-parent", "jar", "1.0", null, null, null, false);
        assertEquals(1, parent.getChildNodes().size());
        Node child = parent.getFirstChildNode();
        return child;
    }

}
