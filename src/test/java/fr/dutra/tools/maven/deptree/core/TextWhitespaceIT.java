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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.text.StrTokenizer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TextWhitespaceIT {

    private static class CheckLine {
        private int depth;
        private String groupId;
        private String artifactId;
        private String packaging;
        private String classifier;
        private String version;
        private String scope;
        private boolean omitted;
        private String description;
        public static CheckLine parse(String line) {
            CheckLine cl = new CheckLine();
            String[] tokens = StrTokenizer.getCSVInstance(line).setDelimiterChar(',').setEmptyTokenAsNull(true).getTokenArray();
            cl.depth = Integer.parseInt(tokens[0]);
            cl.groupId = tokens[1];
            cl.artifactId = tokens[2];
            cl.packaging = tokens[3];
            cl.classifier = tokens[4];
            cl.version = tokens[5];
            cl.scope = tokens[6];
            cl.omitted = Boolean.parseBoolean(tokens[7]);
            cl.description = tokens[8];
            return cl;
        }
    }

    private String filename;

    private InputType type;

    private int index;

    private static List<CheckLine> cls;

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

    @BeforeClass
    public static void setUpCheckFile() throws IOException {
        InputStream is = TextWhitespaceIT.class.getResourceAsStream("/output-files/check.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        cls = new ArrayList<CheckLine>();
        String line = null;
        while((line = r.readLine()) != null) {
            CheckLine cl = CheckLine.parse(line);
            cls.add(cl);
        }
    }

    @Test
    public void test() throws Exception {
        InputStream is = this.getClass().getResourceAsStream(filename);
        Reader r = new InputStreamReader(is, "UTF-8");
        Parser parser = type.newParser();
        Node tree = parser.parse(r);
        this.index = 0;
        checkNode(tree, 0);
    }

    private void checkNode(Node node, int depth) {
        CheckLine cl = cls.get(index++);
        Assert.assertEquals(cl.depth, depth);
        Assert.assertEquals(cl.groupId, node.getGroupId());
        Assert.assertEquals(cl.artifactId, node.getArtifactId());
        Assert.assertEquals(cl.packaging, node.getPackaging());
        Assert.assertEquals(cl.classifier, node.getClassifier());
        Assert.assertEquals(cl.version, node.getVersion());
        Assert.assertEquals(cl.scope, node.getScope());
        Assert.assertEquals(cl.omitted, node.isOmitted());
        Assert.assertEquals(cl.description, node.getDescription());
        for (Node child : node.getChildNodes()) {
            checkNode(child, depth + 1);
        }
    }

}
