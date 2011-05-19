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

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class DotParser extends AbstractLineBasedParser {

    private Map<String, Node> nodes = new HashMap<String, Node>();

    private Node root;

    public Node parse(Reader reader) throws ParseException {

        try {
            this.lines = splitLines(reader);
        } catch (IOException e) {
            throw new ParseException(e);
        }

        if(lines.isEmpty()) {
            return null;
        }

        //root node
        this.parseFirstLine();

        if(root != null) {

            for(; lineIndex < this.lines.size() - 1; lineIndex++) {
                this.parseLine();
            }

        }

        return root;

    }

    private void parseFirstLine() {
        String str = StringUtils.substringBetween(this.lines.get(0), "\"");
        String[] tokens = StringUtils.split(str, ':');
        if(tokens.length != 4) {
            throw new IllegalStateException("Wrong number of tokens: " + tokens.length + " for first line (4 expected)");
        }
        final Node node = new Node(
            tokens[0],
            tokens[1],
            tokens[2],
            null,
            tokens[3],
            null,
            null,
            false
        );
        root = node;
        nodes.put(str, node);
        lineIndex++;
    }

    /**
     * sample line structure:
     * <pre>"fr.dutra.tools.maven.deptree.core:maven-dependency-tree-parser:jar:1.0-SNAPSHOT" -> "org.mockito:mockito-all:jar:1.8.5:test" ;</pre>
     */
    private void parseLine() {
        String line = this.lines.get(this.lineIndex);
        String parentArtifact;
        if(line.contains("->")) {
            parentArtifact = StringUtils.substringBetween(line, "\"");
        } else {
            parentArtifact = extractActiveProjectArtifact();
            line = lines.get(lineIndex);
        }
        Node parent = nodes.get(parentArtifact);
        if(parent != null) {
            String childArtifact;
            if(line.contains("active project artifact:")) {
                childArtifact = extractActiveProjectArtifact();
            } else {
                childArtifact = StringUtils.substringBetween(line, "-> \"", "\" ;");
            }
            Node child = parseArtifactString(childArtifact);
            parent.addChildNode(child);
            nodes.put(childArtifact, child);
        } else {
            throw new IllegalStateException("Cannot find parent artifact: " + parentArtifact);
        }

    }

}
