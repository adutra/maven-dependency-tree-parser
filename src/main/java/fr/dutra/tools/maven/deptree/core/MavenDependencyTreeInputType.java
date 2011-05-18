/**
 * 
 */
package fr.dutra.tools.maven.deptree.core;



/**
 * @author Alexandre Dutra
 *
 */
public enum MavenDependencyTreeInputType {

    TEXT {

        @Override
        public MavenDependencyTreeParser newParser() {
            return new MavenDependencyTreeTextParser();
        }
    },

    DOT {

        @Override
        public MavenDependencyTreeParser newParser() {
            return new MavenDependencyTreeDotParser();
        }
    },

    GRAPHML {

        @Override
        public MavenDependencyTreeParser newParser() {
            return new MavenDependencyTreeGraphmlParser();
        }
    },

    TGF {

        @Override
        public MavenDependencyTreeParser newParser() {
            return new MavenDependencyTreeTgfParser();
        }
    };

    public abstract MavenDependencyTreeParser newParser();

}
