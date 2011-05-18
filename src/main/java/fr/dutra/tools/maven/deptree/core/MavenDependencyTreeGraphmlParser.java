package fr.dutra.tools.maven.deptree.core;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MavenDependencyTreeGraphmlParser extends MavenDependencyTreeAbstractParser {

    public MavenDependencyTreeNode parse(Reader reader) throws MavenDependencyTreeParseException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        try {
            SAXParser saxParser = factory.newSAXParser();
            EventHandler handler = new EventHandler();
            saxParser.parse(new InputSource(reader), handler);
            return handler.getRootNode();
        } catch (ParserConfigurationException e) {
            throw new MavenDependencyTreeParseException(e);
        } catch (SAXException e) {
            throw new MavenDependencyTreeParseException(e);
        } catch (IOException e) {
            throw new MavenDependencyTreeParseException(e);
        }
    }

    private class EventHandler extends DefaultHandler {

        private Map<String, MavenDependencyTreeNode> nodes = new HashMap<String, MavenDependencyTreeNode>();

        private String currentNodeId;

        private boolean insideNodeLabel;

        private MavenDependencyTreeNode root;

        public MavenDependencyTreeNode getRootNode() {
            return root;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            if("node".equals(localName)) {
                currentNodeId = attributes.getValue("", "id");
            }

            if ("edge".equals(localName)){
                String parentNodeId = attributes.getValue("", "source");
                String childNodeId = attributes.getValue("", "target");
                MavenDependencyTreeNode parent = nodes.get(parentNodeId);
                MavenDependencyTreeNode child = nodes.get(childNodeId);
                parent.addChildNode(child);
            }

            insideNodeLabel = "NodeLabel".equals(localName);

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            insideNodeLabel = false;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(insideNodeLabel) {
                String artifact = String.valueOf(ch, start, length).trim();
                MavenDependencyTreeNode node = parseArtifactString(artifact);
                nodes.put(currentNodeId, node);
                if(root == null) {
                    root = node;
                }
                currentNodeId = null;
            }
        }

    }

}