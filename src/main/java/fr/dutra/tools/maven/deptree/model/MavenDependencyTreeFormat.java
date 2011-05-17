/**
 * 
 */
package fr.dutra.tools.maven.deptree.model;



/**
 * @author Alexandre Dutra
 *
 */
public enum MavenDependencyTreeFormat {

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
    };

    public abstract MavenDependencyTreeParser newParser();

}
