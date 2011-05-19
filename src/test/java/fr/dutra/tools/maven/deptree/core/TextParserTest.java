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
import fr.dutra.tools.maven.deptree.core.TextParser;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextParser.class)
public class TextParserTest extends AbstractParserTest {

    @Mock
    private BufferedReader br;

    @Mock
    private Reader r;

    private TextParser parser = new TextParser();

    @Before
    public void setUp() throws Exception {
        PowerMockito.whenNew(BufferedReader.class).withArguments(r).thenReturn(br);
    }

    @Test
    public void test_4_tokens_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("com.acme.org:foo:jar:1.0").
        thenReturn(null);
        Node tree = parser.parse(r);
        checkNode(tree, "com.acme.org", "foo", "jar", "1.0", null, null, null, false);
    }

    @Test
    public void test_5_tokens_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("com.acme.org:foo:jar:1.0:compile").
        thenReturn(null);
        Node tree = parser.parse(r);
        checkNode(tree, "com.acme.org", "foo", "jar", "1.0", "compile", null, null, false);
    }

    @Test
    public void test_6_tokens_with_description_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("com.acme.org:foo:jar:4.4:test (scope not updated to compile)").
        thenReturn(null);
        Node tree = parser.parse(r);
        checkNode(tree, "com.acme.org", "foo", "jar", "4.4", "test", null, "scope not updated to compile", false);
    }

    @Test
    public void test_6_tokens_with_classifier_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("com.acme.org:foo:jar:win-32:4.4:test").
        thenReturn(null);
        Node tree = parser.parse(r);
        checkNode(tree, "com.acme.org", "foo", "jar", "4.4", "test", "win-32", null, false);
    }

    @Test
    public void test_7_tokens_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("com.acme.org:foo:jar:win-32:4.4:test (version managed from 2.1.3)").
        thenReturn(null);
        Node tree = parser.parse(r);
        checkNode(tree, "com.acme.org", "foo", "jar", "4.4", "test", "win-32", "version managed from 2.1.3", false);
    }

    @Test
    public void test_omitted_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("(com.acme.org:foo:jar:4.8.2:test - omitted for conflict with 4.8.1)").
        thenReturn(null);
        Node tree = parser.parse(r);
        checkNode(tree, "com.acme.org", "foo", "jar", "4.8.2", "test", null, "omitted for conflict with 4.8.1", true);
    }

    @Test(expected=IllegalStateException.class)
    public void test_8_tokens_should_fail() throws Exception {
        when(br.readLine()).
        thenReturn("com.acme.org:foo:jar:win-32:4.4:test:wtf (version managed from 2.1.3)").
        thenReturn(null);
        parser.parse(r);
    }

    @Test(expected=IllegalStateException.class)
    public void test_3_tokens_should_fail() throws Exception {
        when(br.readLine()).
        thenReturn("com.acme.org:foo:jar").
        thenReturn(null);
        parser.parse(r);
    }

    @Test
    public void test_parent_child_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("com.acme.org:foo:jar:1.0").
        thenReturn("\\- org.apache.velocity:velocity:jar:1.6.4:compile").
        thenReturn(null);
        Node parent = parser.parse(r);
        checkNode(parent, "com.acme.org", "foo", "jar", "1.0", null, null, null, false);
        assertEquals(1, parent.getChildNodes().size());
        Node child = parent.getFirstChildNode();
        checkNode(child, "org.apache.velocity", "velocity", "jar", "1.6.4", "compile", null, null, false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_active_project_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("com.acme.org:foo:jar:1.0").
        thenReturn("\\- active project artifact:").
        thenReturn("\tartifact = active project artifact:").
        thenReturn("\tartifact = active project artifact:").
        thenReturn("\tartifact = com.acme.org:foo-core-impl:jar:1.0.41-SNAPSHOT:compile;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-core-impl:1.0.41-SNAPSHOT @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-core-impl/pom.xml;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-core-impl:1.0.41-SNAPSHOT @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-core-impl/pom.xml;").
        thenReturn("\tproject: MavenProject: com.acme.org:foo-core-impl:1.0.41-SNAPSHOT @ /opt/jenkins/home/jobs/foo/workspace/trunk/foo-core-impl/pom.xml").
        thenReturn(null);
        Node parent = parser.parse(r);
        checkNode(parent, "com.acme.org", "foo", "jar", "1.0", null, null, null, false);
        assertEquals(1, parent.getChildNodes().size());
        Node child = parent.getFirstChildNode();
        checkNode(child, "com.acme.org", "foo-core-impl", "jar", "1.0.41-SNAPSHOT", "compile", null, null, false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

}
