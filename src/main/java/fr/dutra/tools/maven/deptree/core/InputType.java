/**
 * 
 */
package fr.dutra.tools.maven.deptree.core;



/**
 * @author Alexandre Dutra
 *
 */
public enum InputType {

    TEXT {

        @Override
        public Parser newParser() {
            return new TextParser();
        }
    },

    DOT {

        @Override
        public Parser newParser() {
            return new DotParser();
        }
    },

    GRAPHML {

        @Override
        public Parser newParser() {
            return new GraphmlParser();
        }
    },

    TGF {

        @Override
        public Parser newParser() {
            return new TgfParser();
        }
    };

    public abstract Parser newParser();

}
