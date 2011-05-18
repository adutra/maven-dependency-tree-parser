package fr.dutra.tools.maven.deptree.core;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MavenDependencyTreeTextWhitespaceIT {

    private String filename;

    private MavenDependencyTreeInputType type;

    public MavenDependencyTreeTextWhitespaceIT(String filename, MavenDependencyTreeInputType type) {
        super();
        this.filename = filename;
        this.type = type;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
            { "/text-standard.txt", MavenDependencyTreeInputType.TEXT },
            { "/text-whitespace.txt", MavenDependencyTreeInputType.TEXT },
            { "/text-extended.txt", MavenDependencyTreeInputType.TEXT },
            { "/dot.txt", MavenDependencyTreeInputType.DOT },
            { "/graphml.xml", MavenDependencyTreeInputType.GRAPHML },
            { "/tgf.txt", MavenDependencyTreeInputType.TGF }
        };
        return Arrays.asList(data);
    }

    @Test
    public void test() throws Exception {
        InputStream is = this.getClass().getResourceAsStream(filename);
        Reader r = new InputStreamReader(is, "UTF-8");
        MavenDependencyTreeParser parser = type.newParser();
        MavenDependencyTreeNode tree = parser.parse(r);
        System.out.println(tree);
    }

}
