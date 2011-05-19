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
public class TextWhitespaceIT {

    private String filename;

    private InputType type;

    public TextWhitespaceIT(String filename, InputType type) {
        super();
        this.filename = filename;
        this.type = type;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
            { "/text-standard.txt", InputType.TEXT },
            { "/text-whitespace.txt", InputType.TEXT },
            { "/text-extended.txt", InputType.TEXT },
            { "/dot.txt", InputType.DOT },
            { "/graphml.xml", InputType.GRAPHML },
            { "/tgf.txt", InputType.TGF }
        };
        return Arrays.asList(data);
    }

    @Test
    public void test() throws Exception {
        InputStream is = this.getClass().getResourceAsStream(filename);
        Reader r = new InputStreamReader(is, "UTF-8");
        Parser parser = type.newParser();
        Node tree = parser.parse(r);
        System.out.println(tree);
    }

}
