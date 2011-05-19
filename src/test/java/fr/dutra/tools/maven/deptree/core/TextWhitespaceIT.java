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
            { "/output-files/text-standard.txt", InputType.TEXT },
            { "/output-files/text-whitespace.txt", InputType.TEXT },
            { "/output-files/text-extended.txt", InputType.TEXT },
            { "/output-files/dot.txt", InputType.DOT },
            { "/output-files/graphml.xml", InputType.GRAPHML },
            { "/output-files/tgf.txt", InputType.TGF }
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
