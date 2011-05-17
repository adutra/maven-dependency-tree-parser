package fr.dutra.tools.maven.deptree;

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

import fr.dutra.tools.maven.deptree.model.MavenDependencyNode;
import fr.dutra.tools.maven.deptree.model.MavenDependencyTreeDotParser;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MavenDependencyTreeDotParser.class)
public class MavenDependencyTreeDotParserTest extends AbstractMavenDependencyTreeParserTest {

    @Mock
    private BufferedReader br;

    @Mock
    private Reader r;

    private MavenDependencyTreeDotParser parser = new MavenDependencyTreeDotParser();

    @Before
    public void setUp() throws Exception {
        PowerMockito.whenNew(BufferedReader.class).withArguments(r).thenReturn(br);
    }

    /*
     * digraph "com.acme.org:foo-integration:jar:1.0.41-SNAPSHOT" {
    "com.acme.org:foo-integration:jar:1.0.41-SNAPSHOT" -> "com.acme.org:commons-integration:jar:2.39.0:compile" ;
     */
    @Test
    public void test_root_node_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("}").
        thenReturn(null);
        MavenDependencyNode parent = parser.parse(r);
        checkNode(parent, "com.acme.org", "foo-parent", "jar", "1.0", null, null, null, false);
        assertEquals(0, parent.getChildNodes().size());
    }

    @Test
    public void test_5_tokens_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("    \"com.acme.org:foo-parent:jar:1.0\" -> \"com.acme.org:foo-child:jar:2.0:compile\" ; ").
        thenReturn("}").
        thenReturn(null);
        MavenDependencyNode parent = parser.parse(r);
        MavenDependencyNode child = checkParentAndGetFirstChild(parent);
        checkNode(child, "com.acme.org", "foo-child", "jar", "2.0", "compile", null, null, false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_6_tokens_with_description_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("    \"com.acme.org:foo-parent:jar:1.0\" -> \"com.acme.org:foo:jar:4.4:test (scope not updated to compile)\" ; ").
        thenReturn("}").
        thenReturn(null);
        MavenDependencyNode parent = parser.parse(r);
        MavenDependencyNode child = checkParentAndGetFirstChild(parent);
        checkNode(child, "com.acme.org", "foo", "jar", "4.4", "test", null, "scope not updated to compile", false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_6_tokens_with_classifier_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("    \"com.acme.org:foo-parent:jar:1.0\" -> \"com.acme.org:foo:jar:win-32:4.4:test\" ; ").
        thenReturn("}").
        thenReturn(null);
        MavenDependencyNode parent = parser.parse(r);
        MavenDependencyNode child = checkParentAndGetFirstChild(parent);
        checkNode(child, "com.acme.org", "foo", "jar", "4.4", "test", "win-32", null, false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_7_tokens_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("    \"com.acme.org:foo-parent:jar:1.0\" -> \"com.acme.org:foo:jar:win-32:4.4:test (version managed from 2.1.3)\" ; ").
        thenReturn("}").
        thenReturn(null);
        MavenDependencyNode parent = parser.parse(r);
        MavenDependencyNode child = checkParentAndGetFirstChild(parent);
        checkNode(child, "com.acme.org", "foo", "jar", "4.4", "test", "win-32", "version managed from 2.1.3", false);
        assertNotNull(child.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_omitted_should_succeed() throws Exception {
        when(br.readLine()).
        thenReturn("digraph \"com.acme.org:foo-parent:jar:1.0\" {").
        thenReturn("    \"com.acme.org:foo-parent:jar:1.0\" -> \"(com.acme.org:foo:jar:4.8.2:test - omitted for conflict with 4.8.1)\" ; ").
        thenReturn("}").
        thenReturn(null);
        MavenDependencyNode parent = parser.parse(r);
        MavenDependencyNode child = checkParentAndGetFirstChild(parent);
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
        thenReturn("    \"com.acme.org:bar-parent:jar:1.0\" -> \"com.acme.org:bar-child:jar:2.0\" ; ").
        thenReturn("}").
        thenReturn(null);
        parser.parse(r);
    }

    private MavenDependencyNode checkParentAndGetFirstChild(MavenDependencyNode parent) {
        checkNode(parent, "com.acme.org", "foo-parent", "jar", "1.0", null, null, null, false);
        assertEquals(1, parent.getChildNodes().size());
        MavenDependencyNode child = parent.getFirstChildNode();
        return child;
    }

}
